package com.dapl.halaladvisor.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.billingclient.api.Purchase
import com.dapl.halaladvisor.databinding.FragmentSubscriptionBinding
import com.dapl.halaladvisor.productscan.BillingManager
import com.dapl.halaladvisor.productscan.SubscriptionRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SubscriptionFragment : Fragment(), BillingManager.Callback {

    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = _binding!!

    private var billingManager: BillingManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)

        Log.d(TAG, "onCreateView: setting up billing client")
        billingManager = BillingManager(requireContext().applicationContext, this).also {
            it.startConnection()
        }

        binding.btnClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.cardFree.setOnClickListener {
            // "if user selct free , then same tap to scan ala"
            Log.d(TAG, "cardFree clicked: keeping free plan")
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.cardPremium.setOnClickListener {
            Log.d(TAG, "cardPremium clicked: starting purchase flow")
            startPurchase()
        }

        binding.btnSubscribe.setOnClickListener {
            Log.d(TAG, "btnSubscribe clicked: starting purchase flow")
            startPurchase()
        }

        return binding.root
    }

    private fun startPurchase() {
        val manager = billingManager
        if (manager == null) {
            Log.e(TAG, "startPurchase: billingManager is null")
            return
        }
        manager.launchPurchaseFlow(requireActivity())
    }

    // region BillingManager.Callback

    override fun onPremiumPurchased(purchase: Purchase) {
        Log.d(TAG, "onPremiumPurchased: premium active, token=${purchase.purchaseToken}")
        if (!isAdded) return

        val productId = purchase.products.firstOrNull() ?: BillingManager.PREMIUM_PRODUCT_ID
        val expiryDate = computeExpiryDate(purchase.purchaseTime)

        // After the Play purchase succeeds: tell our backend to activate Premium,
        // then re-check subscription_status to confirm the user is now premium.
        SubscriptionRepository.activateAndRefresh(
            requireContext().applicationContext,
            purchase.purchaseToken,
            productId,
            expiryDate
        ) { isPremium, message ->
            if (!isAdded) return@activateAndRefresh
            if (isPremium) {
                Toast.makeText(requireContext(), "Welcome to Premium!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    message ?: "Could not activate Premium, please contact support",
                    Toast.LENGTH_LONG
                ).show()
            }
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    /**
     * Best-effort expiry the client can report for a monthly plan: one month from the
     * purchase time, formatted as the backend expects ("yyyy-MM-dd HH:mm:ss").
     * The server remains the source of truth via subscription_status.
     */
    private fun computeExpiryDate(purchaseTimeMillis: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = if (purchaseTimeMillis > 0L) purchaseTimeMillis else System.currentTimeMillis()
            add(Calendar.MONTH, 1)
        }
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(calendar.time)
    }

    override fun onPriceReady(formattedPrice: String) {
        Log.d(TAG, "onPriceReady: $formattedPrice")
        if (!isAdded) return
        requireActivity().runOnUiThread {
            binding.btnSubscribe.text = "Subscribe for $formattedPrice / month"
        }
    }

    override fun onPurchaseCancelled() {
        Log.d(TAG, "onPurchaseCancelled")
    }

    override fun onBillingError(message: String) {
        Log.e(TAG, "onBillingError: $message")
        if (!isAdded) return
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    // endregion

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: releasing billing client")
        billingManager?.release()
        billingManager = null
        _binding = null
    }

    companion object {
        private const val TAG = "SUBSCRIPTION_FRAGMENT"
        fun newInstance() = SubscriptionFragment()
    }
}
