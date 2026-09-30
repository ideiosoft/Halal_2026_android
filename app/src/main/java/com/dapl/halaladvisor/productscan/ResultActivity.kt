package com.dapl.halaladvisor.productscan


import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dapl.halaladvisor.Activity.AddProductBottomSheet
import com.dapl.halaladvisor.BuildConfig
import com.dapl.halaladvisor.R
import com.dapl.halaladvisor.databinding.ActivityResultBinding
import com.dapl.halaladvisor.extra.ApiService
import com.google.android.gms.ads.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultActivity : AppCompatActivity() {

    private var productInfoData: String = ""
    private var ingredientsData: String = ""
    private var prohibitionData: String = ""
    lateinit var apiService: ApiService

    private val binding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }




        apiService = Retrofit.Builder()
            .baseUrl("https://admin.halaladvisor.com.au/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)

        val barcode = intent.getStringExtra("barcode") ?: return
        binding.txtBarcode.text = barcode

        setupTabs()
       fetchProduct(barcode)

        binding.back.setOnClickListener { finish() }

        setupAds()
    }

    private fun setupAds() {
        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()
        binding.adView2.loadAd(adRequest)

        binding.adView2.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d("ADS", "Ad Loaded")
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.d("ADS", "Ad Failed: ${error.message}")
            }
        }
    }


//    @SuppressLint("CheckResult")
//    fun fetchProduct(barcode: String) {
//
//        showLoading()
//
//        // ✅ STEP 1: YOUR API FIRST
//        apiService.getProductDetails(barcode)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ response ->
//
//                if (response?.status == true) {
//
//                    // ✅ YOUR API SUCCESS
//                    showData(
//                        response.product_name ?: "Unknown",
//                        response.brand ?: "Unknown",
//                        response.category ?: "Unknown",
//                        barcode,
//                        response.country ?: "Unknown",
//                        response.ingredients ?: "",
//                        response.image_url
//                    )
//
//                } else {
//                    // ❌ STATUS FALSE → FALLBACK
//                    callOpenFoodFlow(barcode)
//                }
//
//            }, {
//                // ❌ ERROR → FALLBACK
//                callOpenFoodFlow(barcode)
//            })
//    }

    @SuppressLint("CheckResult")
    fun fetchProduct(barcode: String) {

        showLoading()

        // ✅ STEP 1 → WOOLWORTHS
        WoolworthsApiClient.api.searchProduct(barcode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                try {

                    val product = response.Products
                        ?.flatMap { it.Products ?: emptyList() }
                        ?.firstOrNull()

                    if (product != null) {

                        val ingredients =
                            product.AdditionalAttributes?.ingredients ?: ""

                        showData(
                            product.DisplayName ?: "Unknown",
                            product.Brand ?: "Unknown",
                            product.AdditionalAttributes?.sapcategoryname ?: "Unknown",
                            barcode,
                            product.AdditionalAttributes?.countryoforigin ?: "Australia",
                            ingredients,
                            product.LargeImageFile
                        )

                    } else {

                        // ❌ NOT FOUND → YOUR API
                        fetchFromAdminApi(barcode)
                    }

                } catch (e: Exception) {
                    fetchFromAdminApi(barcode)
                }

            }, {

                // ❌ ERROR → YOUR API
                fetchFromAdminApi(barcode)
            })
    }
    @SuppressLint("CheckResult")
    private fun fetchFromAdminApi(barcode: String) {

        apiService.getProductDetails(barcode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                if (response?.status == true) {

                    // ✅ ADMIN API SUCCESS
                    showData(
                        response.product_name ?: "Unknown",
                        response.brand ?: "Unknown",
                        response.category ?: "Unknown",
                        barcode,
                        response.country ?: "Unknown",
                        response.ingredients ?: "",
                        response.image_url
                    )

                } else {

                    // ❌ FALLBACK → OPENFOOD
                    callOpenFoodFlow(barcode)
                }

            }, {

                // ❌ ERROR → OPENFOOD
                callOpenFoodFlow(barcode)
            })
    }

    @SuppressLint("CheckResult")
    private fun callOpenFoodFlow(barcode: String) {

        OpenFoodProductClient.api.getProduct(barcode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                val product = response.product

                if (response.status == 1 && product != null) {

                    val ingredients = cleanIngredients(product.ingredients_text)

                    showData(
                        product.product_name ?: "Unknown",
                        product.brands ?: "Unknown",
                        product.categories ?: "Unknown",
                        barcode,
                        cleanCountry(product.countries),
                        ingredients,
                        product.image_front_url
                    )

                } else {
                    // ❌ NO PRODUCT → GO TO ADD PRODUCT
                    showNoProduct(barcode)
                }

            }, {
                // ❌ ERROR → GO TO ADD PRODUCT
                showNoProduct(barcode)
            })
    }




//    @SuppressLint("CheckResult")
//    private fun fetchProduct(barcode: String) {
//
//     showLoading()
//
//        OpenFoodProductClient.api.getProduct(barcode)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ response ->
//
//                val product = response.product
//
//                if (response.status == 1 && product != null) {
//
//                    val ingredients = cleanIngredients(product.ingredients_text)
//
//                    if (ingredients.isNotBlank()) {
//
//                        // ✅ DIRECT SHOW (ALL from FIRST API)
//                        showData(
//                            product.product_name ?: "Unknown",
//                            product.brands ?: "Unknown",
//                            product.categories ?: "Unknown",
//                            barcode,
//                            cleanCountry(product.countries),
//                            ingredients,
//                            product.image_front_url
//                        )
//
//                    } else {
//                        showData(
//                            product.product_name ?: "Unknown",
//                            product.brands ?: "Unknown",
//                            product.categories ?: "Unknown",
//                            barcode,
//                            cleanCountry(product.countries),
//                            ingredients,
//                            product.image_front_url
//                        )
//
//                        // 🔥 SEARCH ONLY FOR INGREDIENTS
////                        val fallbackName = product.product_name ?: "Unknown"
////                        val fallbackBrand = product.brands ?: "Unknown"
////                        val fallbackCategory = product.categories ?: "Unknown"
////
////                        val searchQuery = listOfNotNull(
////                            product.product_name,
////                            product.brands
////                        ).joinToString(" ").ifBlank { barcode }
////
////                        searchProductByName(
////                            barcode,
////                            searchQuery,
////                            fallbackName,
////                            fallbackBrand,
////                            product.categories?:fallbackCategory,
////                            product.image_front_url
////                        )
//                    }
//
//                } else {
//
//                    binding.txtNoProduct.visibility = View.VISIBLE
//                    binding.progressBar.visibility = View.GONE
//
//                    // 🔥 FINAL FALLBACK (NO FIRST API DATA)
////                    searchProductByName(
////                        barcode,
////                        barcode,
////                        "Unknown",
////                        "Unknown",
////                        "Unknown",
////                        null
////                    )
//                }
//
//            }, {
//                binding.txtNoProduct.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
//
//
//
//                // 🔥 ERROR CASE
////                searchProductByName(
////                    barcode,
////                    barcode,
////                    "Unknown",
////                    "Unknown",
////                    "Unknown",
////                    null
////                )
//            })
//    }

    @SuppressLint("CheckResult")
    private fun searchProductByName(
        barcode: String,
        searchQuery: String,
        fallbackName: String,
        fallbackBrand: String,
        fallbackCategory: String,
        fallbackImage: String?
    ) {

        OpenFoodSearchClient.api.searchProduct(searchQuery.ifBlank { barcode })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                val products = response.products ?: emptyList()

                // ✅ STEP 1: product WITH ingredients
                val productWithIngredients = products.firstOrNull {
                    val ing = cleanIngredients(it.ingredients_text)
                    ing.isNotBlank()
                }

                // ✅ STEP 2: fallback ANY product
                val bestProduct = productWithIngredients ?: products.firstOrNull()

                val ingredients = cleanIngredients(bestProduct?.ingredients_text)

                if (bestProduct != null) {

                    // ✅ ONLY INGREDIENTS from second API
                    showData(
                        fallbackName,          // 🔒 LOCKED
                        fallbackBrand,         // 🔒 LOCKED
                        bestProduct.categories?:fallbackCategory,      // 🔒 LOCKED
                        barcode,
                        cleanCountry(bestProduct.countries),
                        ingredients.ifBlank { "Ingredients not available" },
                        fallbackImage          // 🔒 LOCKED
                    )

                } else {
                    showNoProduct(barcode)
                }

            }, {
                showNoProduct(barcode)
            })
    }

    ////



//    @SuppressLint("CheckResult")
//    private fun fetchProduct(barcode: String) {
//
//        binding.progressBar.visibility = View.VISIBLE
//        binding.main.visibility = View.GONE
//        binding.txtNoProduct.visibility = View.GONE
//
//        GoUpcApiClient.api.getProduct(barcode, "299eaf0bac83f111712a6c825350e304093efde91857d9ce5bf26c3f1959119d")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ response ->
//
//                val product = response.product
//
//                if (product != null) {
//
//                    val ingredients = cleanIngredients(product.ingredients?.text)
//
//                    if (ingredients.isNotBlank()) {
//
//                        showData(
//                            product.name ?: "",
//                            product.brand ?: "",
//                            product.category ?: "",
//                            barcode,
//                            getCountryOfManufacturing(product.specs),
//                            ingredients,
//                            product.imageUrl
//                        )
//
//                    } else {
//                        // 👉 STEP 2
//                        fetchFromOpenFoodBarcode(barcode)
//                    }
//
//                } else {
//                    fetchFromOpenFoodBarcode(barcode)
//                }
//
//            }, {
//                fetchFromOpenFoodBarcode(barcode)
//            })
//    }

    @SuppressLint("CheckResult")
    private fun searchOpenFoodByName(
        barcode: String,
        fallbackName: String,
        fallbackBrand: String,
        fallbackCategory: String,
        fallbackImage: String?
    ) {

        OpenFoodSearchClient.api.searchProduct(fallbackName.ifBlank { "food" })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                val products = response.products ?: emptyList()

                // ✅ STEP 1: Filter valid ingredients
                val validProducts = products.filter {
                    val ing = cleanIngredients(it.ingredients_text)
                    ing.isNotBlank() && ing.lowercase() != "null"
                }

                // ✅ STEP 2: Prefer product WITH image


                val bestProduct = validProducts
                    .sortedWith(
                        compareByDescending<Product> {
                            !it.image_front_url.isNullOrBlank()   // ✅ image priority
                        }.thenByDescending {
                            it.ingredients?.size ?: 0            // ✅ ingredients quality
                        }
                    )
                    .firstOrNull()

                val finalImage = fallbackImage

                if (bestProduct != null) {

                    val ingredients = cleanIngredients(bestProduct.ingredients_text)

                    showData(
                        bestProduct.product_name ?: fallbackName,
                        bestProduct.brands ?: fallbackBrand,
                        bestProduct.categories ?: fallbackCategory,
                        barcode,
                        cleanCountry(bestProduct.countries),
                        ingredients,
                        finalImage
                    )

                } else {
                    binding.progressBar.visibility = View.GONE
                    showNoProduct(barcode)
                }

            }, {
                binding.progressBar.visibility = View.GONE
                showNoProduct(barcode)
            })
    }



    @SuppressLint("CheckResult")
    private fun fetchFromOpenFoodBarcode(barcode: String) {

        OpenFoodProductClient.api.getProduct(barcode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                val product = response.product

                if (response.status == 1 && product != null) {

                    val ingredients = cleanIngredients(product.ingredients_text)

                    if (ingredients.isNotBlank()) {

                        showData(
                            product.product_name ?: "Unknown",
                            product.brands ?: "Unknown",
                            product.categories ?: "Unknown",
                            barcode,
                            cleanCountry(product.countries),
                            ingredients,
                            product.image_front_url
                        )

                    } else {
                        // 👉 STEP 3 (LAST)
                        searchOpenFoodByName(
                            barcode,
                            product.product_name ?: "",
                            product.brands ?: "",
                            product.categories ?: "",
                            product.image_front_url
                        )
                    }

                } else {
                    searchOpenFoodByName(barcode, "", "", "", null)
                }

            }, {
                searchOpenFoodByName(barcode, "", "", "", null)
            })
    }







    private fun showNoProduct(barcode: String) {

        binding.progressBar.visibility = View.GONE
        binding.main.visibility = View.GONE

        val sheet = AddProductBottomSheet(barcode)
        sheet.onProductAdded?.let {
            fetchProduct(barcode)

        }
        sheet.show(supportFragmentManager, "AddProductSheet")
    }



    @SuppressLint("CheckResult")
    private fun showData(
        productName: String,
        brand: String,
        category: String,
        barcode: String,
        country: String,
        ingredients: String,
        image: String?
    ) {

        // ❌ UI yaha mat dikhao
        // binding.main.visibility = View.VISIBLE
        // binding.progressBar.visibility = View.GONE

        productInfoData = """
Product Name: $productName
Brand: $brand
Category: $category
Country of Manufacturing: $country
Barcode: $barcode
""".trimIndent()

        binding.txtProductName.text = productName

        Glide.with(this)
            .load(image)
            .into(binding.productImage)

        // ✅ CASE 1: Ingredients available
        if (ingredients.isNotBlank()) {

            ingredientsData = ingredients
            classifyFromText("$productName $ingredients")

            binding.textForAll.text = productInfoData

            showFinalUI() // 🔥 SHOW ONLY HERE
            return
        }

        // 🔥 CASE 2: AI flow
        extractTextFromImage(image ?: "") { ocrText ->

            val smartName = buildFinalName(productName, brand, ocrText)

            generateIngredientsFromAI(
                apiKey = BuildConfig.OPENAI_API_KEY,
                productName = smartName,
                brand = brand ?: "",
                imageUrl = image
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ aiIngredients ->

                    val finalIngredients = aiIngredients.takeIf {
                        it.isNotBlank() &&
                                !it.contains("not available", true)
                    }

                    if (!finalIngredients.isNullOrBlank()) {

                        ingredientsData = finalIngredients
                        classifyFromText("$smartName $finalIngredients")

                    } else {

                        ingredientsData = "Ingredients not available"

                        updateResultUI(
                            FoodClassifier.Result(
                                "MUSHBOOH",
                                "Ingredients not available"
                            )
                        )
                    }

                    binding.textForAll.text = productInfoData

                    showFinalUI() // 🔥 FINAL UI HERE

                }, {

                    updateResultUI(
                        FoodClassifier.Result(
                            "MUSHBOOH",
                            "AI failed"
                        )
                    )

                    showFinalUI() // 🔥 EVEN ON ERROR
                })
        }
    }

    fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.main.visibility = View.GONE
    }

    fun showFinalUI() {
        binding.progressBar.visibility = View.GONE
        binding.main.visibility = View.VISIBLE
    }

    fun generateIngredientsFromAI(
        apiKey: String,
        productName: String,
        brand: String,
        imageUrl: String?
    ): Single<String> {

        return Single.create { emitter ->

            val client = OkHttpClient()

            try {

                val contentArray = JSONArray()

                val textBlock = JSONObject()
                textBlock.put("type", "input_text")
                textBlock.put(
                    "text",
                    """
Extract or guess the ingredients of this food product.

Product Name: $productName
Brand: $brand

Rules:
- If image shows ingredients, extract exactly.
- Otherwise guess realistic ingredients.
- Return only comma-separated ingredients.
- No explanation.
- If unsure return: Ingredients not available
                """.trimIndent()
                )

                contentArray.put(textBlock)

                if (!imageUrl.isNullOrBlank()) {
                    val imageBlock = JSONObject()
                    imageBlock.put("type", "input_image")
                    imageBlock.put("image_url", imageUrl)
                    contentArray.put(imageBlock)
                }

                val inputArray = JSONArray()

                val userMessage = JSONObject()
                userMessage.put("role", "user")
                userMessage.put("content", contentArray)

                inputArray.put(userMessage)

                val requestBodyJson = JSONObject()
                requestBodyJson.put("model", "gpt-4.1-mini")
                requestBodyJson.put("input", inputArray)
                requestBodyJson.put("max_output_tokens", 200)

                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    requestBodyJson.toString()
                )

                val request = Request.Builder()
                    .url("https://api.openai.com/v1/responses")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {

                    override fun onFailure(call: Call, e: IOException) {
                        if (!emitter.isDisposed) {
                            emitter.onError(e)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {

                        try {
                            val body = response.body?.string()

                            if (body.isNullOrBlank()) {
                                emitter.onError(Throwable("Empty response"))
                                return
                            }

                            val json = JSONObject(body)

                            val content = json
                                .getJSONArray("output")
                                .getJSONObject(0)
                                .getJSONArray("content")
                                .getJSONObject(0)
                                .getString("text")

                            if (!emitter.isDisposed) {
                                emitter.onSuccess(content.trim())
                            }

                        } catch (e: Exception) {
                            if (!emitter.isDisposed) {
                                emitter.onError(e)
                            }
                        }
                    }
                })

            } catch (e: Exception) {
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            }
        }
    }

    fun cleanCountry(country: String?): String {

        if (country.isNullOrBlank()) return "Unknown"

        return country
            .replace("en:", "")   // remove prefix
            .split(",")           // if multiple countries
            .firstOrNull()        // take first
            ?.trim()
            ?: "Unknown"
    }

    private fun classifyFromText(text: String?) {

        if (text.isNullOrBlank()) {
            updateResultUI(
                FoodClassifier.Result(
                    "MUSHBOOH",
                    "Ingredients not available"
                )
            )
            return
        }

        val result = FoodClassifier.classify(text)
        updateResultUI(result)
    }



    private fun updateResultUI(result: FoodClassifier.Result) {

        binding.productLabel.text = result.halalStatus

        when (result.halalStatus) {

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

            "DOUBTFUL" -> {
                binding.halalCard.setCardBackgroundColor("#FFC107".toColorInt())
                binding.checkImg.setImageResource(R.drawable.ic_doubtful)
                binding.checkImg.setColorFilter("#FFC107".toColorInt())

            }

            else -> {
                binding.halalCard.setCardBackgroundColor("#4CAF50".toColorInt())
                binding.checkImg.setColorFilter("#4CAF50".toColorInt())
                binding.checkImg.setImageResource(R.drawable.ic_check)
            }
        }

        prohibitionData = """
Status: ${result.halalStatus}

Reason:
${result.reason}
""".trimIndent()
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
//        binding.productInfoText.setTextColor("#4CAF50".toColorInt())

        binding.textForAll.text = productInfoData
    }

    private fun selectIngredients() {

        resetTabs()

        binding.ingredientsInfoView.visibility = View.VISIBLE
//        binding.ingredientsInfoText.setTextColor("#4CAF50".toColorInt())

        binding.textForAll.text = ingredientsData
    }

    private fun selectProhibition() {

        resetTabs()

        binding.ProhibitionInfoView.visibility = View.VISIBLE
//        binding.ProhibitionInfoText.setTextColor("#4CAF50".toColorInt())

        binding.textForAll.text =
            prohibitionData.ifBlank { "No analysis available" }

    }



    fun cleanIngredients(text: String?): String {

        if (text.isNullOrEmpty()) return ""

        return text
            .replace(Regex("(?i)made in.*"), "")
            .trim()
    }


    fun getCountryOfManufacturing(specs: List<List<String>>?): String {

        specs?.forEach { spec ->

            if (spec.size >= 2) {

                val key = spec[0].trim().lowercase()
                val value = spec[1].trim()

                when {
                    key == "country of manufacturing" -> return value
                    key == "countries" -> return value
                    key.contains("manufact") -> return value
                    key.contains("country") -> return value
                }
            }
        }

        return "Australia"
    }

    fun extractTextFromImage(imageUrl: String, callback: (String) -> Unit) {

        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    val image = InputImage.fromBitmap(resource, 0)

                    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                    recognizer.process(image)
                        .addOnSuccessListener { visionText ->
                            callback(visionText.text)
                        }
                        .addOnFailureListener {
                            callback("")
                        }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
    fun buildSmartName(
        apiName: String?,
        brand: String?,
        ocrText: String
    ): String {

        val cleanOCR = ocrText
            .lines()
            .map { it.trim() }
            .filter { it.length > 3 }
            .take(3) // top 2-3 lines

        val ocrName = cleanOCR.joinToString(" ")

        return listOfNotNull(
            apiName,
            brand,
            ocrName
        ).joinToString(" ").trim()
    }

    fun cleanOCRText(raw: String): String {

        val ignoreWords = listOf(
            "nutrition", "energy", "ingredients",
            "made", "net", "g", "kg",
            "for", "with", "or", "snack", "snactg",
            "per", "serving", "barcode"
        )

        return raw
            .lines()
            .map { it.trim() }
            .filter { line ->
                line.length in 3..<40 &&   // 🔥 avoid long junk lines
                        ignoreWords.none { word ->
                            line.lowercase().contains(word)
                        }
            }
            .take(4) // 🔥 only top lines
            .joinToString(" ")
    }

    fun formatProductName(text: String): String {

        return text
            .lowercase()
            .split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") {
                it.replaceFirstChar { char -> char.uppercase() }
            }
    }

    fun buildFinalName(
        apiName: String?,
        brand: String?,
        ocrText: String
    ): String {

        val cleanedOCR = cleanOCRText(ocrText)

        val combined = listOfNotNull(
            brand,
            apiName,
            cleanedOCR
        ).joinToString(" ")

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
}