package com.dapl.halaladvisor.Fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapl.halaladvisor.Adapter.ScanHistoryAdapter
import com.dapl.halaladvisor.Model.ScanHistoryItem
import com.android.billingclient.api.Purchase
import com.dapl.halaladvisor.databinding.FragmentProductScannerBinding
import com.dapl.halaladvisor.productscan.BillingManager
import com.dapl.halaladvisor.productscan.ResultFragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class ProductScannerFragment : Fragment(), BillingManager.Callback {

    private var _binding: FragmentProductScannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var previewView: PreviewView
    private var isScanned = false

    private var billingManager: BillingManager? = null
    private var premiumSelected = true
    private var isPurchaseInProcess = false

    private fun isValidBarcode(code: String): Boolean {
        return code.matches(Regex("^[0-9]{12,13}$"))
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startCamera()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductScannerBinding.inflate(inflater, container, false)

        Log.d(TAG, "onCreateView: setting up billing client")
        billingManager = BillingManager(requireContext().applicationContext, this).also {
            it.startConnection()
        }

        binding.btnSelectFree.setOnClickListener {
            selectPlan(isPremium = false)
        }

        binding.btnSelectPremium.setOnClickListener {
            selectPlan(isPremium = true)
        }

        binding.btnCloseSubscription.setOnClickListener {
            // Dismiss the plan picker — continue on the free plan and don't show it again
            com.dapl.halaladvisor.Utility.PrefUtils.setFreePlanSelected(requireContext(), true)
            showScannerContent()
        }

        binding.btnSubAction.setOnClickListener {
            if (binding.btnSubAction.text.toString().contains("Subscribe", ignoreCase = true)) {
                Log.d(TAG, "btnSubAction (Subscribe) clicked: starting purchase flow")
                startPurchase()
            } else {
                // User chose the free plan — remember it so we skip this screen next time
                com.dapl.halaladvisor.Utility.PrefUtils.setFreePlanSelected(requireContext(), true)
                showScannerContent()
            }
        }

        binding.layoutTapToScan.setOnClickListener {
            checkCameraPermission()
        }

        binding.edtBarcode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val barcode = s.toString()
                if (barcode.length == 13 && isValidBarcode(barcode)) {
                    binding.edtBarcode.clearFocus()
                    openResult(barcode)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        previewView = binding.previewView
        // Premium = ad-free: only initialise/load the banner for non-premium users
        if (!com.dapl.halaladvisor.productscan.SubscriptionRepository.isPremium()) {
            MobileAds.initialize(requireContext())
            val adRequest = AdRequest.Builder().build()
            binding.adView2.loadAd(adRequest)

            binding.adView2.adListener = object : AdListener() {
                override fun onAdLoaded() { Log.d("ADS", "Ad Loaded") }
                override fun onAdFailedToLoad(error: LoadAdError) { Log.d("ADS", "Ad Failed: ${error.message}") }
            }
        }

        setupRecentScans()
        // Apply cached usage synchronously first (no flicker), then refresh in the background
        com.dapl.halaladvisor.productscan.ScanUsageRepository.cached?.let { applyUsage(it) }
        fetchScanUsage()
        applyPremiumGating()
        
        // Default to the recommended Premium plan (matches the design)
        selectPlan(isPremium = true)

        binding.txtViewAll.setOnClickListener {
            // Free users: "View all" is a blurred upsell — not clickable, do nothing
            if (com.dapl.halaladvisor.productscan.SubscriptionRepository.isPremium()) {
                startActivity(Intent(requireContext(), com.dapl.halaladvisor.Activity.ActivityScanHistory::class.java))
            }
        }

        binding.btnUpgrade.setOnClickListener {
            binding.layoutSubscriptionSelection.visibility = View.VISIBLE
            binding.layoutScannerContent.visibility = View.GONE
        }

        // Scan limit overlay action — upgrade goes straight to the Play purchase
        // sheet for premium, skipping the plan-selection screen.
        binding.btnUpgradeLimit.setOnClickListener {
            Log.d(TAG, "btnUpgradeLimit clicked: launching purchase flow directly")
            premiumSelected = true
            startPurchase()
        }

        decidePlanSelection()

        return binding.root
    }

    /**
     * Decides whether to show the free/premium plan picker.
     *
     * - Subscribed (premium) users never see it — straight to the scanner with premium perks.
     * - Non-subscribers see it only the very first time they open the app.
     * - Returning non-subscribers go straight to the scanner (free flow, with the monthly
     *   scan-limit check) without the picker.
     */
    private fun decidePlanSelection() {
        val context = requireContext()
        val isPremium = com.dapl.halaladvisor.productscan.SubscriptionRepository.isPremium()
        val seenBefore = com.dapl.halaladvisor.Utility.PrefUtils.isPlanSelectionSeen(context)

        if (isPremium) {
            // Subscribed: never show the picker again
            com.dapl.halaladvisor.Utility.PrefUtils.setPlanSelectionSeen(context, true)
            showScannerContent()
            return
        }

        if (!seenBefore) {
            // Very first app open for a non-subscriber → show the free/premium choice once
            com.dapl.halaladvisor.Utility.PrefUtils.setPlanSelectionSeen(context, true)
            showPlanSelection()
        } else {
            // Returning free user → scanner with the monthly scan-limit check
            showScannerContent()
        }
    }

    private fun showPlanSelection() {
        binding.layoutSubscriptionSelection.visibility = View.VISIBLE
        binding.layoutScannerContent.visibility = View.GONE
    }

    private fun showScannerContent() {
        binding.layoutSubscriptionSelection.visibility = View.GONE
        binding.layoutScannerContent.visibility = View.VISIBLE
        // Premium: no blur, no ads, no scan counter. Free: blurred recent scans + counter.
        applyPremiumGating()
        // If the user has already used up this month's free scans, surface the limit
        // screen instead of the scanner. Use cached usage for an instant decision (no flicker);
        // the in-flight fetchScanUsage() will re-confirm against the server.
        com.dapl.halaladvisor.productscan.ScanUsageRepository.cached?.let { applyUsage(it) }
    }

    private fun selectPlan(isPremium: Boolean) {
        premiumSelected = isPremium
        // Card styling is fixed (outlined-white Free, filled-grey Premium) to match the design.
        // Selection only drives the primary button label and the terms footer.
        if (isPremium) {
            binding.btnSubAction.text = "Subscribe for $1.99 / month"
            binding.txtTerms.visibility = View.VISIBLE
        } else {
            binding.btnSubAction.text = "Start Scanning Now"
            binding.txtTerms.visibility = View.GONE
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            cameraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        binding.previewView.visibility = View.VISIBLE
        binding.layoutTapToScan.visibility = View.GONE
        binding.btnInstantCheck.visibility = View.GONE

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }
            val barcodeScanner = BarcodeScanning.getClient()
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext())) { imageProxy ->
                if (isScanned) {
                    imageProxy.close()
                    return@setAnalyzer
                }
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    barcodeScanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                val value = barcode.rawValue
                                if (!value.isNullOrEmpty() && !isScanned) {
                                    isScanned = true
                                    openResult(value)
                                    break
                                }
                            }
                        }
                        .addOnCompleteListener { imageProxy.close() }
                } else {
                    imageProxy.close()
                }
            }
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun openResult(barcode: String) {
        (requireActivity() as? com.dapl.halaladvisor.MainActivity)?.let { mainActivity ->
            mainActivity.setView(ResultFragment.newInstance(barcode), 123)
        }
    }

    private fun fetchScanUsage() {
        com.dapl.halaladvisor.productscan.ScanUsageRepository.refresh(requireContext()) { data ->
            if (_binding == null) return@refresh
            applyUsage(data)
        }
    }

    private fun applyUsage(data: com.dapl.halaladvisor.productscan.ScanUsageData) {
        if (_binding == null) return
        updateUsageUi(data.usedScans, data.maxScans, data.remainingScans, data.resetDate)
    }

    private fun updateUsageUi(usedScans: Int, maxScans: Int, remainingScans: Int, resetDate: String?) {
        binding.txtUsageInfo.text = "$usedScans of $maxScans free scans used this month"
        binding.txtMonthlyScansCount.text = "$usedScans / $maxScans"
        binding.progressBarMonthly.max = maxScans
        binding.progressBarMonthly.progress = usedScans

        // Premium = unlimited scans, so never block them
        val isPremium = com.dapl.halaladvisor.productscan.SubscriptionRepository.isPremium()
        if (!isPremium && (remainingScans <= 0 || usedScans >= maxScans)) {
            showScanLimitReached(maxScans, resetDate)
        } else {
            binding.layoutScanLimitReached.visibility = View.GONE
        }

        // Re-apply premium gating in case status changed since the screen was built
        applyPremiumGating()
    }

    private fun showScanLimitReached(maxScans: Int, resetDate: String?) {
        binding.txtLimitSubtitle.text =
            "You've used all $maxScans free scans for this month. Upgrade to continue shopping – no limits, no ads."

        val formatted = formatResetDate(resetDate)
        if (formatted != null) {
            binding.txtLimitResetDate.text = "Your free scans reset on $formatted"
            binding.txtLimitResetDate.visibility = View.VISIBLE
        } else {
            binding.txtLimitResetDate.visibility = View.GONE
        }

        binding.layoutScanLimitReached.visibility = View.VISIBLE
    }

    private fun formatResetDate(resetDate: String?): String? {
        if (resetDate.isNullOrBlank()) return null
        return try {
            val parser = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            val formatter = java.text.SimpleDateFormat("d MMM yyyy", java.util.Locale.US)
            parser.parse(resetDate)?.let { formatter.format(it) } ?: resetDate
        } catch (e: Exception) {
            resetDate
        }
    }

    private fun setupRecentScans() {
        binding.rvRecentScans.layoutManager = LinearLayoutManager(requireContext())
        loadRecentScans()
    }

    private fun loadRecentScans() {
        // Show only the last 3 scans on the scanner screen
        com.dapl.halaladvisor.productscan.HistoryRepository.fetchList(requireContext(), page = 1, limit = 3) { items ->
            if (_binding == null) return@fetchList
            binding.rvRecentScans.adapter = ScanHistoryAdapter(items.take(3))
        }
    }

    private fun startPurchase() {
        val manager = billingManager
        if (manager == null) {
            Log.d(TAG, "startPurchase: billingManager is null")
            return
        }
        isPurchaseInProcess = true
        manager.launchPurchaseFlow(requireActivity())
    }

    // region BillingManager.Callback

    override fun onPremiumPurchased(purchase: Purchase) {
        Log.d(TAG, "onPremiumPurchased: premium active, token=${purchase.purchaseToken}")
        if (_binding == null || !isAdded) return

        // Strictly only hit the activation API if the user actually clicked "Subscribe"
        // in this session. Automatic restores from Google Play are ignored here because
        // SubscriptionRepository.prefetch() handles the source of truth from the backend.
        if (!isPurchaseInProcess) {
            Log.d(TAG, "onPremiumPurchased: automatic restore detected, skipping activate_subscription API")
            // Still update UI in case the prefetch finished
            applyPremiumGating()
            return
        }
        
        isPurchaseInProcess = false

        val productId = purchase.products.firstOrNull() ?: BillingManager.PREMIUM_PRODUCT_ID
        val expiryDate = computeExpiryDate(purchase.purchaseTime)

        // Confirm with our backend (activate_subscription -> subscription_status)
        com.dapl.halaladvisor.productscan.SubscriptionRepository.activateAndRefresh(
            requireContext().applicationContext,
            purchase.purchaseToken,
            productId,
            expiryDate
        ) { isPremium, message ->
            if (_binding == null || !isAdded) return@activateAndRefresh
            if (isPremium) {
                showSubscriptionSuccessDialog()
                // Premium = ad-free + unlimited: drop overlays, blur, ads and scan counter
                binding.layoutScanLimitReached.visibility = View.GONE
                showScannerContent()
                fetchScanUsage()
            } else {
                android.widget.Toast.makeText(
                    requireContext(),
                    message ?: "Could not activate Premium, please contact support",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showSubscriptionSuccessDialog() {
        val dialog = android.app.Dialog(requireContext())
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.dapl.halaladvisor.R.layout.dialog_subscription_success)
        dialog.window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)

        val btnDone = dialog.findViewById<android.widget.Button>(com.dapl.halaladvisor.R.id.btnDone)
        btnDone.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Best-effort expiry the client reports for a monthly plan: one month from the
     * purchase time, formatted as the backend expects ("yyyy-MM-dd HH:mm:ss").
     */
    private fun computeExpiryDate(purchaseTimeMillis: Long): String {
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = if (purchaseTimeMillis > 0L) purchaseTimeMillis else System.currentTimeMillis()
            add(java.util.Calendar.MONTH, 1)
        }
        return java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US).format(calendar.time)
    }

    override fun onPriceReady(formattedPrice: String) {
        Log.d(TAG, "onPriceReady: $formattedPrice")
        if (_binding == null || !isAdded) return
        requireActivity().runOnUiThread {
            if (_binding == null) return@runOnUiThread
            if (premiumSelected) {
                binding.btnSubAction.text = "Subscribe for $1.99 / month"
            }
        }
    }

    override fun onPurchaseCancelled() {
        Log.d(TAG, "onPurchaseCancelled")
        isPurchaseInProcess = false
    }

    override fun onBillingError(message: String) {
        Log.e(TAG, "onBillingError: $message")
        isPurchaseInProcess = false
        if (_binding == null || !isAdded) return
        requireActivity().runOnUiThread {
            if (_binding == null) return@runOnUiThread
            android.widget.Toast.makeText(
                requireContext(), message, android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    // endregion

    /**
     * Premium = ad-free + unlimited scans. For premium users hide the scan-count card,
     * the banner ad, the upgrade banner and the recent-scans blur; show them otherwise.
     * Driven by [SubscriptionRepository.isPremium], which reflects the confirmed
     * subscription_status from the backend.
     */
    private fun applyPremiumGating() {
        if (_binding == null) return
        val isPremium = com.dapl.halaladvisor.productscan.SubscriptionRepository.isPremium()
        val gone = if (isPremium) View.GONE else View.VISIBLE

        // Scan counter ("X of Y free scans used") — premium is unlimited, so hide it
        binding.txtUsageInfo.visibility = gone
        binding.cardMonthlyScans.visibility = gone

        // Banner ad + upgrade banner
        binding.adView2.visibility = gone
        binding.cardUpgrade.visibility = gone

        // Never show the scan-limit overlay to premium users
        if (isPremium) binding.layoutScanLimitReached.visibility = View.GONE

        // Recent scans: blurred upsell for free users, fully visible for premium
        if (isPremium) removeBlurEffect() else applyBlurEffect()
    }

    private fun applyBlurEffect() {
        // Free users: still show the recent-scans panel with the last 3 scans, but
        // blur it and block interaction so it acts as a premium upsell.
        binding.relRecentHeader.visibility = View.VISIBLE
        binding.layoutRecentScansContainer.visibility = View.VISIBLE

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val blur = android.graphics.RenderEffect.createBlurEffect(
                25f, 25f, android.graphics.Shader.TileMode.CLAMP
            )
            // Blur the list items and the header ("Recent Scans" + "View all")
            binding.rvRecentScans.setRenderEffect(blur)
            binding.relRecentHeader.setRenderEffect(blur)
        } else {
            // Pre-Android 12 fallback: dim the views since RenderEffect is unavailable
            binding.rvRecentScans.alpha = 0.35f
            binding.relRecentHeader.alpha = 0.35f
        }

        // Overlay is clickable=true, so it intercepts all touches on the list below it.
        binding.layoutRecentScansBlur.visibility = View.VISIBLE
    }

    private fun removeBlurEffect() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            binding.rvRecentScans.setRenderEffect(null)
            binding.relRecentHeader.setRenderEffect(null)
        }
        binding.rvRecentScans.alpha = 1f
        binding.relRecentHeader.alpha = 1f
        binding.layoutRecentScansBlur.visibility = View.GONE
        binding.relRecentHeader.visibility = View.VISIBLE
        binding.layoutRecentScansContainer.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        binding.edtBarcode.clearFocus()
        binding.edtBarcode.text.clear()
        isScanned = false
        fetchScanUsage()
        loadRecentScans()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: releasing billing client")
        billingManager?.release()
        billingManager = null
        _binding = null
    }

    companion object {
        private const val TAG = "PRODUCT_SCANNER"
    }
}