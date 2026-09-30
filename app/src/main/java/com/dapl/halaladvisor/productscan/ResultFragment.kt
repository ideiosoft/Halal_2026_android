package com.dapl.halaladvisor.productscan

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dapl.halaladvisor.Activity.AddProductBottomSheet
import com.dapl.halaladvisor.Fragment.ProductScannerFragment
import com.dapl.halaladvisor.R
import com.dapl.halaladvisor.databinding.ActivityResultBinding
import com.dapl.halaladvisor.extra.ApiService
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.dapl.halaladvisor.Utility.PrefUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class ResultFragment : Fragment() {

    private var productInfoData: String = ""
    private var ingredientsData: String = ""
    private var prohibitionData: String = ""
    private var triggerList: List<String> = emptyList()
    private lateinit var apiService: ApiService

    private var _binding: ActivityResultBinding? = null
    private val binding get() = _binding!!

    private var barcode: String? = null

    private val disposables = CompositeDisposable()
    private var scanCounted = false

    private var interstitialAd: InterstitialAd? = null
    private var pendingShowAd = false
    private var resultsRevealed = false

    private var historyAdded = false
    private var lastProductName: String = ""
    private var lastBrand: String = ""
    private var lastBarcode: String = ""

    companion object {
        // How long to wait for the interstitial to load before just showing results
        private const val AD_WAIT_TIMEOUT_MS = 4000L


        fun newInstance(barcode: String): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putString("barcode", barcode)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcode = arguments?.getString("barcode")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ActivityResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barcode = this.barcode ?: run {
            parentFragmentManager.popBackStack()
            return
        }

        apiService = Retrofit.Builder().baseUrl("https://admin.halaladvisor.com.au/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
            .create(ApiService::class.java)

        binding.txtBarcode.text = barcode

        setupTabs()
        fetchProduct(barcode)

        binding.back.setOnClickListener {
            openScannerFragment()
        }
        setupAds()

        binding.tabLayout.visibility = View.GONE


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    if (isVisible) {
                        openScannerFragment()
                    }
                }
            })


    }

    private fun openScannerFragment() {

        parentFragmentManager.beginTransaction().replace(
            R.id.fragment_container, ProductScannerFragment()
        ).addToBackStack(null).commit()
    }

    private fun setupAds() {
        // Paid users are fully ad-free on the result screen: skip banners and interstitial
        if (SubscriptionRepository.isPremium()) {
            binding.adView2.visibility = View.GONE
            binding.adView3.visibility = View.GONE
            return
        }
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adView2.loadAd(adRequest)
        binding.adView3.loadAd(adRequest)
        loadInterstitial()
    }

    /** Preload the interstitial so it's ready by the time the scan result is available. */
    private fun loadInterstitial() {
        if (SubscriptionRepository.isPremium()) return // premium users see no ads
        val context = context ?: return
        InterstitialAd.load(
            context,
            getString(R.string.interstitial_ad_unit_id),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d("ADS", "Interstitial loaded")
                    if (pendingShowAd) {
                        pendingShowAd = false
                        showAdThenReveal()
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    Log.d("ADS", "Interstitial failed: ${error.message}")
                    if (pendingShowAd) {
                        pendingShowAd = false
                        revealResults()
                    }
                }
            }
        )
    }

    @SuppressLint("CheckResult")
    fun fetchProduct(barcode: String) {

        showLoading()

        WoolworthsApiClient.api.searchProduct(barcode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                val product = response.Products
                    ?.flatMap { it.Products ?: emptyList() }
                    ?.firstOrNull()

                if (product != null) {

                    processProduct(
                        productName = product.DisplayName ?: "Unknown",
                        brand = product.Brand ?: "Unknown",
                        category = product.AdditionalAttributes?.sapcategoryname ?: "Unknown",
                        barcode = barcode,
                        country = product.AdditionalAttributes?.countryoforigin ?: "Australia",
                        ingredients = product.AdditionalAttributes?.ingredients ?: "",
                        image = product.LargeImageFile,
                        lifestyleClaim = product.AdditionalAttributes?.lifestyleclaim,
                        allergenContains = product.AdditionalAttributes?.allergencontains
                    )

                } else {
                    fetchFromAdminApi(barcode)
                }

            }, {
                fetchFromAdminApi(barcode)
            })
    }

    @SuppressLint("CheckResult")
    private fun processProduct(
        productName: String,
        brand: String,
        category: String,
        barcode: String,
        country: String,
        ingredients: String,
        image: String?,
        lifestyleClaim: String? = "",
        allergenContains: String? = ""
    ) {

        val cleanedIngredients = cleanIngredients(ingredients)

        if (cleanedIngredients.isNotBlank()) {

            showData(
                productName,
                brand,
                category,
                barcode,
                country,
                cleanedIngredients,
                image,
                lifestyleClaim,
                allergenContains
            )
            return
        }

        searchWoolworthsByName(
            productName,
            brand,
            category,
            barcode,
            country,
            image,
            lifestyleClaim,
            allergenContains
        )
    }

    @SuppressLint("CheckResult")
    private fun searchWoolworthsByName(
        productName: String,
        brand: String,
        category: String,
        barcode: String,
        country: String,
        image: String?,
        lifestyleClaim: String?,
        allergenContains: String?
    ) {

        Log.e(
            "HALAL_DEBUG",
            """
=====================================
SEARCH WOOLWORTHS BY NAME
=====================================
ProductName = $productName
Brand = $brand
=====================================
        """.trimIndent()
        )

        WoolworthsApiClient.api.searchProduct(productName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                Log.e(
                    "HALAL_DEBUG",
                    """
WOOLWORTHS RESPONSE

Products Count = ${
                        response.Products
                            ?.flatMap { it.Products ?: emptyList() }
                            ?.size ?: 0
                    }
                """.trimIndent()
                )

                val allProducts = response.Products
                    ?.flatMap { it.Products ?: emptyList() }
                    ?: emptyList()

                allProducts.forEachIndexed { index, item ->

                    Log.e(
                        "HALAL_DEBUG",
                        """
RESULT #$index

DisplayName = ${item.DisplayName}
Brand = ${item.Brand}

LifestyleClaim = ${item.AdditionalAttributes?.lifestyleclaim}
AllergenContains = ${item.AdditionalAttributes?.allergencontains}
Ingredients = ${item.AdditionalAttributes?.ingredients}
ProductImages = ${item.AdditionalAttributes?.productimages}
                    """.trimIndent()
                    )
                }

                val product = allProducts.firstOrNull {

                    val titleMatch =
                        it.DisplayName?.contains(productName, true) == true

                    val brandMatch =
                        brand.isNotBlank() &&
                                it.Brand?.contains(brand, true) == true

                    titleMatch || brandMatch

                } ?: allProducts.firstOrNull()

                Log.e(
                    "HALAL_DEBUG",
                    """
SELECTED PRODUCT

DisplayName = ${product?.DisplayName}
Brand = ${product?.Brand}

LifestyleClaim = ${product?.AdditionalAttributes?.lifestyleclaim}
AllergenContains = ${product?.AdditionalAttributes?.allergencontains}
Ingredients = ${product?.AdditionalAttributes?.ingredients}
ProductImages = ${product?.AdditionalAttributes?.productimages}
                """.trimIndent()
                )

                val ingredients =
                    product?.AdditionalAttributes?.ingredients

                if (!ingredients.isNullOrBlank()) {

                    Log.e(
                        "HALAL_DEBUG",
                        """
CALLING showData()

LifestyleClaim = ${product.AdditionalAttributes.lifestyleclaim}
AllergenContains = ${product.AdditionalAttributes.allergencontains}
                    """.trimIndent()
                    )

                    showData(
                        productName,
                        brand,
                        category,
                        barcode,
                        country,
                        ingredients,
                        image,
                        product.AdditionalAttributes?.lifestyleclaim,
                        product.AdditionalAttributes?.allergencontains
                    )

                    return@subscribe
                }

                val productImages =
                    product?.AdditionalAttributes?.productimages

                Log.e(
                    "HALAL_DEBUG",
                    """
INGREDIENTS NOT FOUND

LifestyleClaim = ${product?.AdditionalAttributes?.lifestyleclaim}
AllergenContains = ${product?.AdditionalAttributes?.allergencontains}

Going to OCR
                """.trimIndent()
                )

                runOcrFallback(
                    productName,
                    brand,
                    category,
                    barcode,
                    country,
                    image,
                    product?.AdditionalAttributes?.lifestyleclaim,
                    product?.AdditionalAttributes?.allergencontains,
                    productImages
                )

            }, { error ->

                Log.e(
                    "HALAL_DEBUG",
                    "WOOLWORTHS NAME SEARCH FAILED = ${error.message}"
                )

                runOcrFallback(
                    productName,
                    brand,
                    category,
                    barcode,
                    country,
                    image,
                    lifestyleClaim,
                    allergenContains,
                    null
                )

            })
    }
    private fun runOcrFallback(
        productName: String,
        brand: String,
        category: String,
        barcode: String,
        country: String,
        image: String?,
        lifestyleClaim: String?,
        allergenContains: String?,
        productImages: String?
    ) {

        var ocrImage = image

        if (!productImages.isNullOrBlank()) {

            val secondImage = productImages
                .split(",")
                .firstOrNull { it.contains("_2") }

            if (secondImage != null) {

                ocrImage =
                    "https://cdn1.woolworths.media/content/wowproductimages/large/$secondImage"
            }
        }

        // No image available
        if (ocrImage.isNullOrBlank()) {

            showData(
                productName,
                brand,
                category,
                barcode,
                country,
                "",
                image,
                lifestyleClaim,
                allergenContains
            )
            return
        }

        extractTextFromImage(ocrImage) { ocrText ->

            val ingredients =
                extractIngredientsFromOCR(ocrText)

            Log.e(
                "HALAL_DEBUG",
                """
RAW OCR:
$ocrText

EXTRACTED INGREDIENTS:
$ingredients
        """.trimIndent()
            )

            requireActivity().runOnUiThread {

                showData(
                    productName,
                    brand,
                    category,
                    barcode,
                    country,
                    ingredients,
                    image,
                    lifestyleClaim,
                    allergenContains
                )
            }
        }
    }
    private fun extractIngredientsFromOCR(text: String): String {

        if (text.isBlank()) return ""

        val cleaned = text
            .replace("\n", " ")
            .replace(Regex("\\s+"), " ")

        val ingredientsRegex = Regex(
            "(?i)ingredients\\s*:?\\s*(.*?)(nutrition information|allergen|allergy advice|keep frozen|storage|cooking instructions|www\\.|ph\\.|manufactured|made in|product of|$)"
        )

        val match = ingredientsRegex.find(cleaned)

        if (match != null) {
            return "Ingredients: " + match.groupValues[1].trim()
        }

        return cleaned
    }
    @SuppressLint("CheckResult")
    private fun fetchFromAdminApi(barcode: String) {
        apiService.getProductDetails(barcode).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                if (response?.status == true) {
                    processProduct(
                        response.product_name ?: "Unknown",
                        response.brand ?: "Unknown",
                        response.category ?: "Unknown",
                        barcode,
                        response.country ?: "Unknown",
                        response.ingredients ?: "",
                        response.image_url
                    )
                } else {
                    callOpenFoodFlow(barcode)
                }
            }, {
                callOpenFoodFlow(barcode)
            })
    }

    @SuppressLint("CheckResult")
    private fun callOpenFoodFlow(barcode: String) {
        OpenFoodProductClient.api.getProduct(barcode).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                val product = response.product
                if (response.status == 1 && product != null) {
                    processProduct(
                        product.product_name ?: "Unknown",
                        product.brands ?: "Unknown",
                        product.categories ?: "Unknown",
                        barcode,
                        cleanCountry(product.countries),
                        product.ingredients_text ?: "",
                        product.image_front_url
                    )
                } else {
                    showNoProduct(barcode)
                }
            }, {
                showNoProduct(barcode)
            })
    }

    private fun showNoProduct(barcode: String) {

        if (_binding == null) return

        binding.progressBar.visibility = View.GONE
        binding.main.visibility = View.GONE

        incrementScanSilently()

        val sheet = AddProductBottomSheet(barcode)

        sheet.onProductAdded = {
            fetchProduct(barcode)
        }

        sheet.onDismissCallback = {

            openScannerFragment()
        }

        sheet.show(parentFragmentManager, "AddProductSheet")
    }

    @SuppressLint("CheckResult")
    private fun showData(
        productName: String,
        brand: String,
        category: String,
        barcode: String,
        country: String,
        ingredients: String,
        image: String?,
        lifestyleClaim: String? = "",
        allergenContains: String? = ""
    ) {

        if (_binding == null) return

        // Remember product details for the silent history_add call
        lastProductName = productName
        lastBrand = brand
        lastBarcode = barcode

        val cleanedIngredients = cleanIngredients(ingredients)

        Log.e(
            "HALAL_DEBUG",
            """
=====================================
SHOW DATA CALLED
=====================================
Product: $productName
Brand: $brand
Category: $category
Barcode: $barcode

LifestyleClaim: $lifestyleClaim
AllergenContains: $allergenContains

Ingredients:
$cleanedIngredients
=====================================
        """.trimIndent()
        )

        productInfoData = """
Product Name: $productName
Brand: $brand
Category: $category
Country of Manufacturing: $country
Barcode: $barcode
""".trimIndent()

        binding.txtProductName.text = productName

        // If the title is in a non-English (non-Latin) script, translate it to English
        // and swap it in once the translation arrives. The original is shown meanwhile.
        val sourceLang = guessSourceLanguage(productName)
        if (sourceLang != null) {
            translateProductNameToEnglish(productName, sourceLang) { translated ->
                if (_binding == null) return@translateProductNameToEnglish
                if (translated.isNotBlank() && !translated.equals(productName, ignoreCase = true)) {
                    binding.txtProductName.text = translated
                    productInfoData = productInfoData.replace(
                        "Product Name: $productName",
                        "Product Name: $translated"
                    )
                    // Keep the tab text in sync if Product Info is currently shown.
                    if (binding.productInfoView.visibility == View.VISIBLE) {
                        binding.textForAll.text = productInfoData
                    }
                    lastProductName = translated
                }
            }
        }

        Glide.with(this)
            .load(image)
            .into(binding.productImage)

        ingredientsData = buildString {

            if (cleanedIngredients.isNotBlank()) {
                append(cleanedIngredients)
            }

            if (!allergenContains.isNullOrBlank()) {

                if (isNotEmpty()) {
                    append("\n\n")
                }

                append("Allergens:\n")
                append(allergenContains)
            }

            if (isEmpty()) {
                append("No information available")
            }
        }

        Log.e(
            "HALAL_DEBUG",
            """
WOOLWORTHS CHECK

LifestyleClaim = $lifestyleClaim
AllergenContains = $allergenContains
        """.trimIndent()
        )

        // ====================================================
        // WOOLWORTHS IS SOURCE OF TRUTH
        // ====================================================
        // Only an explicit halal/lifestyle claim can decide the status here.
        // Allergens (Gluten, Wheat, Milk, Soy, ...) are NOT a halal signal — they are
        // dietary/intolerance info and must never set the status or appear as triggers.

        if (!lifestyleClaim.isNullOrBlank()) {

            Log.e(
                "HALAL_DEBUG",
                """
USING WOOLWORTHS RESULT ONLY

FoodClassifier SHOULD NOT RUN

LifestyleClaim = $lifestyleClaim
            """.trimIndent()
            )

            val status = normalizeHalalStatus(lifestyleClaim)

            Log.e(
                "HALAL_DEBUG",
                "WOOLWORTHS STATUS = $status"
            )

            updateResultUI(
                FoodClassifier.Result(
                    halalStatus = status,
                    reason = if (status == "HALAL") "" else "Some $status ingredients found.",
                    triggers = emptyList()
                )
            )

            prohibitionData = buildString {

                appendLine("Status: $status")

                if (status != "HALAL") {

                    appendLine()
                    appendLine("Reason:")
                    appendLine("Some $status ingredients found.")
                }
            }

            Log.e(
                "HALAL_DEBUG",
                """
RETURNING FROM WOOLWORTHS BLOCK

Status = $status
            """.trimIndent()
            )

            binding.textForAll.text = productInfoData
            showFinalUI()
            return
        }

        // ====================================================
        // FALLBACK TO CLASSIFIER
        // ====================================================

        Log.e(
            "HALAL_DEBUG",
            """
CLASSIFIER FALLBACK

No Woolworths lifestyleClaim

Calling classifyFromText()
        """.trimIndent()
        )

        classifyFromText("$productName $cleanedIngredients")

        binding.textForAll.text = productInfoData
        showFinalUI()
    }

    fun showLoading() {
        if (_binding == null) return
        binding.progressBar.visibility = View.VISIBLE
        binding.main.visibility = View.GONE
    }

    fun showFinalUI() {
        if (_binding == null) return
        incrementScanSilently()
        // Don't reveal the results yet — play the interstitial ad first, then reveal.
        showAdThenReveal()
    }

    /**
     * Shows the interstitial ad (if ready) and only reveals the scan results once it's dismissed.
     * If the ad isn't ready yet, waits briefly for it to load and then falls back to revealing
     * the results so the user is never stuck behind a missing ad.
     */
    private fun showAdThenReveal() {
        if (_binding == null || resultsRevealed) return

        // Premium users are never shown interstitial ads
        if (SubscriptionRepository.isPremium()) {
            revealResults()
            return
        }

        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    revealResults()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    interstitialAd = null
                    revealResults()
                }
            }
            ad.show(requireActivity())
        } else {
            // Ad not ready yet — keep the loader up and wait, with a timeout fallback.
            pendingShowAd = true
            binding.root.postDelayed({
                if (pendingShowAd) {
                    pendingShowAd = false
                    revealResults()
                }
            }, AD_WAIT_TIMEOUT_MS)
        }
    }

    private fun revealResults() {
        if (resultsRevealed) return
        resultsRevealed = true
        if (_binding == null) return
        binding.progressBar.visibility = View.GONE
        binding.main.visibility = View.VISIBLE
    }

    /**
     * Silently bumps the user's monthly scan count once per scan, regardless of whether the
     * product lookup succeeded or failed. Failures are swallowed (logged only) so the UI is
     * never affected.
     */
    @SuppressLint("CheckResult")
    private fun incrementScanSilently() {
        if (scanCounted || SubscriptionRepository.isPremium()) return

        val context = context ?: return
        val uId = PrefUtils.getProfileObject(context)?.u_id?.toLongOrNull()
        if (uId == null) {
            Log.e("SCAN_USAGE_API", "No valid saved user id; skipping increment_scan")
            return
        }

        scanCounted = true

        disposables.add(
            ScanUsageApiClient.api.incrementScan(IncrementScanRequest(uId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Log.d("SCAN_USAGE_API", "increment_scan ok: ${response.message}")
                    response.data?.let { ScanUsageRepository.update(it) }
                }, { error ->
                    Log.e("SCAN_USAGE_API", "increment_scan failed: ${error.message}")
                })
        )
    }

    fun generateIngredientsFromAI(
        apiKey: String, productName: String, brand: String, imageUrl: String?
    ): Single<String> {
        return Single.create { emitter ->
            val client = OkHttpClient()
            try {
                val contentArray = JSONArray()
                val textBlock = JSONObject()
                textBlock.put("type", "input_text")
                textBlock.put(
                    "text",
                    "Extract or guess the ingredients of this food product.\n\nProduct Name: $productName\nBrand: $brand\n\nRules:\n- If image shows ingredients, extract exactly.\n- Otherwise guess realistic ingredients.\n- Return only comma-separated ingredients.\n- No explanation.\n- If unsure return: Ingredients not available"
                )
                contentArray.put(textBlock)
                if (!imageUrl.isNullOrBlank()) {
                    val imageBlock = JSONObject()
                    imageBlock.put("type", "input_image")
                    imageBlock.put("image_url", imageUrl)
                    contentArray.put(imageBlock)
                }
                val inputArray = JSONArray()
                val userMessage = JSONObject().put("role", "user").put("content", contentArray)
                inputArray.put(userMessage)
                val requestBodyJson =
                    JSONObject().put("model", "gpt-4.1-mini").put("input", inputArray)
                        .put("max_output_tokens", 200)
                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(), requestBodyJson.toString()
                )
                val request = Request.Builder().url("https://api.openai.com/v1/responses")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json").post(requestBody).build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        if (!emitter.isDisposed) emitter.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val body = response.body?.string() ?: ""
                            val json = JSONObject(body)
                            val content =
                                json.getJSONArray("output").getJSONObject(0).getJSONArray("content")
                                    .getJSONObject(0).getString("text")
                            if (!emitter.isDisposed) emitter.onSuccess(content.trim())
                        } catch (e: Exception) {
                            if (!emitter.isDisposed) emitter.onError(e)
                        }
                    }
                })
            } catch (e: Exception) {
                if (!emitter.isDisposed) emitter.onError(e)
            }
        }
    }

    /**
     * Inspects the text for characters from a non-Latin script and returns the most likely
     * ISO source-language code for it (MyMemory has no auto-detect, so we must supply one).
     * Returns null when the text looks like plain Latin/English, in which case no translation
     * is attempted.
     */
    private fun guessSourceLanguage(text: String): String? {
        if (text.isBlank()) return null
        return when {
            Regex("[\\u3040-\\u30FF]").containsMatchIn(text) -> "ja" // Hiragana / Katakana
            Regex("[\\uAC00-\\uD7AF]").containsMatchIn(text) -> "ko" // Hangul
            Regex("[\\u4E00-\\u9FFF]").containsMatchIn(text) -> "zh" // CJK (Chinese / Kanji)
            Regex("[\\u0600-\\u06FF]").containsMatchIn(text) -> "ar" // Arabic
            Regex("[\\u0400-\\u04FF]").containsMatchIn(text) -> "ru" // Cyrillic
            Regex("[\\u0590-\\u05FF]").containsMatchIn(text) -> "he" // Hebrew
            Regex("[\\u0370-\\u03FF]").containsMatchIn(text) -> "el" // Greek
            Regex("[\\u0E00-\\u0E7F]").containsMatchIn(text) -> "th" // Thai
            Regex("[\\u0900-\\u097F]").containsMatchIn(text) -> "hi" // Devanagari (Hindi)
            else -> null
        }
    }

    /**
     * Translates a product title to English via the free MyMemory API
     * (https://mymemory.translated.net). The callback is delivered on the main thread.
     * On any failure the original text is returned unchanged so the UI is never left blank.
     */
    @SuppressLint("CheckResult")
    private fun translateProductNameToEnglish(
        name: String, sourceLang: String, callback: (String) -> Unit
    ) {
        val single = Single.create<String> { emitter ->
            val client = OkHttpClient()
            try {
                val query = URLEncoder.encode(name, "UTF-8")
                val url =
                    "https://api.mymemory.translated.net/get?q=$query&langpair=$sourceLang|en"
                val request = Request.Builder().url(url).get().build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        if (!emitter.isDisposed) emitter.onSuccess(name)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val body = response.body?.string() ?: ""
                            val translated = JSONObject(body)
                                .getJSONObject("responseData")
                                .getString("translatedText")
                            if (!emitter.isDisposed) emitter.onSuccess(translated.trim().ifBlank { name })
                        } catch (e: Exception) {
                            if (!emitter.isDisposed) emitter.onSuccess(name)
                        }
                    }
                })
            } catch (e: Exception) {
                if (!emitter.isDisposed) emitter.onSuccess(name)
            }
        }

        disposables.add(
            single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> callback(result) }, { callback(name) })
        )
    }

    fun cleanCountry(country: String?): String {
        if (country.isNullOrBlank()) return "Unknown"
        return country.replace("en:", "").split(",").firstOrNull()?.trim() ?: "Unknown"
    }

    private fun classifyFromText(text: String?) {

        Log.e(
            "HALAL_DEBUG",
            """
=====================================
CLASSIFY FROM TEXT
=====================================
INPUT:
$text
=====================================
        """.trimIndent()
        )

        if (text.isNullOrBlank()) {

            Log.e(
                "HALAL_DEBUG",
                """
CLASSIFIER RECEIVED EMPTY TEXT

Returning:
Status = MUSHBOOH
Reason = Ingredients not available
            """.trimIndent()
            )

            updateResultUI(
                FoodClassifier.Result(
                    "MUSHBOOH",
                    "Ingredients not available"
                )
            )
            return
        }

        Log.e(
            "HALAL_DEBUG",
            "CALLING FoodClassifier.classify()"
        )

        val result = FoodClassifier.classify(text)

        Log.e(
            "HALAL_DEBUG",
            """
=====================================
CLASSIFIER RESULT
=====================================
Status  = ${result.halalStatus}
Reason  = ${result.reason}
Triggers= ${result.triggers}
=====================================
        """.trimIndent()
        )

        updateResultUI(result)
    }

    private fun updateResultUI(result: FoodClassifier.Result) {

        Log.e(
            "HALAL_DEBUG",
            """
=====================================
UPDATE RESULT UI
=====================================
Status  = ${result.halalStatus}
Reason  = ${result.reason}
Triggers= ${result.triggers}
=====================================
        """.trimIndent()
        )

        if (_binding == null) {
            Log.e(
                "HALAL_DEBUG",
                "UPDATE RESULT UI ABORTED -> binding is null"
            )
            return
        }

        // Halal status is always one of: HALAL, HARAM, MUSHBOOH
        val status = normalizeHalalStatus(result.halalStatus)

        binding.productLabel.text = status
        triggerList = result.triggers

        Log.e(
            "HALAL_DEBUG",
            """
UPDATING UI

Label = $status
TriggerCount = ${result.triggers.size}
        """.trimIndent()
        )

        when (status) {

            "HARAM" -> {

                binding.halalCard.setCardBackgroundColor("#F44336".toColorInt())

                binding.checkImg.setImageResource(R.drawable.ic_cross)

                binding.checkImg.setColorFilter("#F44336".toColorInt())
            }

            "MUSHBOOH" -> {

                binding.halalCard.setCardBackgroundColor("#FFC107".toColorInt())

                binding.checkImg.setImageResource(R.drawable.ic_doubtful)

                binding.checkImg.setColorFilter("#FFC107".toColorInt())
            }

            else -> {

                binding.halalCard.setCardBackgroundColor("#4CAF50".toColorInt())

                binding.checkImg.setImageResource(R.drawable.ic_check)
                binding.checkImg.setColorFilter("#4CAF50".toColorInt())


            }
        }

        prohibitionData = buildString {

            appendLine("Status: $status")
            appendLine()
            appendLine("Reason:")
            appendLine(result.reason)

            if (result.triggers.isNotEmpty()) {

                appendLine()
                appendLine("Triggers:")

                result.triggers.forEach {
                    appendLine("• $it")
                }
            }
        }

        Log.e(
            "HALAL_DEBUG",
            """
PROHIBITION DATA GENERATED

$prohibitionData
        """.trimIndent()
        )

        // Save this scan to the user's history (silent)
        addToHistory(status)
    }

    /** Coerces any classifier/source value into one of the three allowed statuses. */
    private fun normalizeHalalStatus(raw: String?): String {
        val s = raw?.trim()?.uppercase().orEmpty()
        return when {
            s.contains("NON-HALAL") || s.contains("NON HALAL") || s.contains("NOT HALAL") ||
                s.contains("HARAM") || s.contains("PORK") || s.contains("ALCOHOL") -> "HARAM"
            s.contains("HALAL") -> "HALAL"
            else -> "MUSHBOOH"
        }
    }

    /** Silently records the scan result to the user's history. Failures are logged only. */
    @SuppressLint("CheckResult")
    private fun addToHistory(halalStatus: String) {
        if (historyAdded) return
        val context = context ?: return
        val uId = PrefUtils.getProfileObject(context)?.u_id?.toLongOrNull()
        if (uId == null) {
            Log.e("HISTORY_ADD", "No valid saved user id; skipping history_add")
            return
        }

        historyAdded = true

        disposables.add(
            ScanUsageApiClient.api.historyAdd(
                HistoryAddRequest(
                    uId = uId,
                    barcode = lastBarcode,
                    productName = lastProductName,
                    brand = lastBrand,
                    halalStatus = halalStatus
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Log.d("HISTORY_ADD", "history_add ok: ${response.message}")
                }, { error ->
                    Log.e("HISTORY_ADD", "history_add failed: ${error.message}")
                })
        )
    }

    private fun setupTabs() {
        selectProductInfo()
        binding.productInfo.setOnClickListener { selectProductInfo() }
        binding.ingredientsInfo.setOnClickListener { selectIngredients() }
        binding.ProhibitionInfo.setOnClickListener { selectProhibition() }
    }

    private fun resetTabs() {
        binding.productInfoView.visibility = View.GONE
        binding.ingredientsInfoView.visibility = View.GONE
        binding.ProhibitionInfoView.visibility = View.GONE
        binding.productInfoText.setTextColor(Color.BLACK)
        binding.ingredientsInfoText.setTextColor(Color.BLACK)
        binding.ProhibitionInfoText.setTextColor(Color.BLACK)
    }

    private fun selectProductInfo() {
        resetTabs()
        binding.productInfoView.visibility = View.VISIBLE
        binding.textForAll.text = productInfoData
    }

    private fun selectIngredients() {
        resetTabs()
        binding.ingredientsInfoView.visibility = View.VISIBLE
        binding.textForAll.text = ingredientsData
    }

    private fun selectProhibition() {
        resetTabs()
        binding.ProhibitionInfoView.visibility = View.VISIBLE
        binding.textForAll.text = prohibitionData.ifBlank { "No analysis available" }
    }

    fun cleanIngredients(text: String?): String {
        if (text.isNullOrEmpty()) return ""
        val withoutHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString()
        } else {
            Html.fromHtml(text).toString()
        }
        return withoutHtml.replace(Regex("(?i)made in.*"), "").trim()
    }

    fun extractTextFromImage(imageUrl: String, callback: (String) -> Unit) {
        Glide.with(this).asBitmap().load(imageUrl).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val image = InputImage.fromBitmap(resource, 0)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                recognizer.process(image)
                    .addOnSuccessListener { visionText -> callback(visionText.text) }
                    .addOnFailureListener { callback("") }
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }

    fun cleanOCRText(raw: String): String {
        val ignoreWords = listOf(
            "nutrition",
            "energy",
            "ingredients",
            "made",
            "net",
            "g",
            "kg",
            "for",
            "with",
            "or",
            "snack",
            "snactg",
            "per",
            "serving",
            "barcode"
        )
        return raw.lines().map { it.trim() }.filter { line ->
            line.length in 3..39 && ignoreWords.none { word ->
                line.lowercase().contains(word)
            }
        }.take(4).joinToString(" ")
    }

    fun formatProductName(text: String): String {
        return text.lowercase().split(" ").filter { it.isNotBlank() }
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }

    fun buildFinalName(apiName: String?, brand: String?, ocrText: String): String {
        val cleanedOCR = cleanOCRText(ocrText)
        val combined = listOfNotNull(brand, apiName, cleanedOCR).joinToString(" ")
        val noDuplicate = removeDuplicates(combined)
        return formatProductName(noDuplicate)
    }

    fun removeDuplicates(text: String): String {
        val words = text.split(" ")
        val uniqueWords = mutableListOf<String>()
        for (word in words) {
            if (uniqueWords.none { it.equals(word, ignoreCase = true) }) {
                uniqueWords.add(word)
            }
        }
        return uniqueWords.joinToString(" ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
        _binding = null
    }
}
