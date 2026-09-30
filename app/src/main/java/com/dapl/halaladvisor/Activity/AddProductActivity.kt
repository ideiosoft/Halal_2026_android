package com.dapl.halaladvisor.Activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat

import com.dapl.halaladvisor.Model.AddProductRequest
import com.dapl.halaladvisor.databinding.ActivityAddProductBinding
import com.dapl.halaladvisor.extra.ApiService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.dapl.halaladvisor.productscan.ResultFragment
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.core.graphics.scale
import androidx.core.graphics.toColorInt
import com.dapl.halaladvisor.BuildConfig

class AddProductBottomSheet(
    private val barcode: String , var onProductAdded: (() -> Unit)? = null , var onDismissCallback: (() -> Unit)? = null
) : BottomSheetDialogFragment() {

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var apiService: ApiService
    private lateinit var cameraExecutor: ExecutorService
    
    private var imageCapture: ImageCapture? = null
    private var capturedIngredientsUri: Uri? = null
    private var productImageUri: Uri? = null
    private var labelImageUri: Uri? = null
    private var selectedLabel: String = ""
    private var aiDetectedStatus: String = ""
    
    private var isCapturingProduct = false
    private var hasAiRun = false
    private var selectedHalalStatus = "UNKNOWN"
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)

        onDismissCallback?.invoke()
    }

    private val pickProductImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            productImageUri = it
            binding.imgProduct.setImageURI(it)
            performOCR(it, binding.edtProductName, "Product Name")
        }
    }

    private val pickLabelImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            labelImageUri = it
            binding.imgLabel.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ActivityAddProductBinding.inflate(inflater, container, false)
        cameraExecutor = Executors.newSingleThreadExecutor()
        
        initApi()

        binding.edtBarcode.setText(barcode)

        // ✅ Step 1: No Product Found
        binding.btnYes.setOnClickListener {
            showRealProductStatus()
        }

        binding.btnNo.setOnClickListener {
            onDismissCallback?.invoke()
            dismiss()
        }


        binding.btnCloseConfirm.setOnClickListener { dismiss() }
        binding.btnCloseUpload.setOnClickListener { dismiss() }
        binding.btnCloseSubmitting.setOnClickListener { dismiss() }

        // ✅ Step 2: Real Product Status
        binding.btnCaptureIngredients.setOnClickListener {
            isCapturingProduct = false
            checkCameraPermission()
        }

        // ✅ Step 3: Camera UI
        binding.btnCaptureImage.setOnClickListener {
            takePhoto()
        }

        // ✅ Step 4: Confirm Ingredients
        binding.btnRetry.setOnClickListener {
            isCapturingProduct = false
            startCamera()
        }

        binding.btnContinue.setOnClickListener {
            showUploadProduct()
        }

        // ✅ Step 5: Upload Product
        binding.btnCaptureProductCamera.setOnClickListener {
            isCapturingProduct = true
            checkCameraPermission()
        }
        
        binding.btnUploadProductGallery.setOnClickListener {
            pickProductImage.launch("image/*")
        }

        binding.btnUploadLabelImage.setOnClickListener {
            pickLabelImage.launch("image/*")
        }

        binding.btnHalal.setOnClickListener { selectLabel("Halal") }
        binding.btnVegan.setOnClickListener { selectLabel("Vegan") }
        binding.btnVegetarian.setOnClickListener { selectLabel("Vegetarian") }
        binding.btnKosher.setOnClickListener { selectLabel("Kosher") }

        binding.btnSubmitProduct.setOnClickListener {
            submitProduct()
        }

        binding.btnCloseSubmitting.setOnClickListener { dismiss() }

        return binding.root
    }

    private fun showRealProductStatus() {
        binding.layoutNoProduct.visibility = View.GONE
        binding.layoutRealProductStatus.visibility = View.VISIBLE
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        binding.layoutNoProduct.visibility = View.GONE
        binding.layoutRealProductStatus.visibility = View.GONE
        binding.layoutConfirmIngredients.visibility = View.GONE
        binding.layoutUploadProduct.visibility = View.GONE
        
        binding.cameraPreview.visibility = View.VISIBLE
        binding.btnCaptureImage.visibility = View.VISIBLE

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.cameraPreview.surfaceProvider
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("AddProduct", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val prefix = if (isCapturingProduct) "product_" else "ingredients_"
        val photoFile = File(requireContext().externalCacheDir, "${prefix}${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                if (isCapturingProduct) {
                    productImageUri = savedUri
                    binding.imgProduct.setImageURI(savedUri)
                    performOCR(savedUri, binding.edtProductName, "Product Name")
                    showUploadProduct()
                } else {
                    showConfirmIngredients(savedUri)
                }
            }
            override fun onError(exception: ImageCaptureException) {
                Log.e("AddProduct", "Photo capture failed: ${exception.message}", exception)
            }
        })
    }

    private fun showConfirmIngredients(uri: Uri) {
        binding.cameraPreview.visibility = View.GONE
        binding.btnCaptureImage.visibility = View.GONE
        binding.layoutConfirmIngredients.visibility = View.VISIBLE
        binding.txtBarcodeConfirm.text = barcode
        binding.imgCapturedIngredients.setImageURI(uri)
        capturedIngredientsUri = uri
        
        performOCR(uri, binding.txtExtractedIngredients, "Ingredients")
    }

    private fun showUploadProduct() {
        binding.cameraPreview.visibility = View.GONE
        binding.btnCaptureImage.visibility = View.GONE
        binding.layoutRealProductStatus.visibility = View.GONE
        binding.layoutConfirmIngredients.visibility = View.GONE
        binding.layoutUploadProduct.visibility = View.VISIBLE
        binding.txtBarcodeUpload.text = barcode
        binding.imgIngredientsPreview.setImageURI(capturedIngredientsUri)
        
        if (productImageUri != null) {
            binding.imgProduct.setImageURI(productImageUri)
        }

        // 🔥 Trigger AI Analysis ONLY if both images are present and hasn't run yet
        if (productImageUri != null && capturedIngredientsUri != null && !hasAiRun) {
            runMagicAI()
        }
    }

    private fun performOCR(uri: Uri, targetView: View, type: String) {
        binding.layoutScanning.visibility = View.VISIBLE
        try {
            val image = InputImage.fromFilePath(requireContext(), uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val resultText = visionText.text
                    if (targetView is EditText) {
                        targetView.setText(resultText)
                    } else if (targetView is TextView) {
                        targetView.text = resultText
                    }
                    binding.layoutScanning.visibility = View.GONE
                    
                    // 🔥 AI only triggers if both images are present and hasn't run
                    if (binding.layoutUploadProduct.visibility == View.VISIBLE && 
                        productImageUri != null && capturedIngredientsUri != null && !hasAiRun) {
                        runMagicAI()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("OCR", "Text recognition failed", e)
                    binding.layoutScanning.visibility = View.GONE
                }
        } catch (e: Exception) {
            Log.e("OCR", "Error preparing image for OCR", e)
            binding.layoutScanning.visibility = View.GONE
        }
    }

    @SuppressLint("CheckResult")
    private fun runMagicAI() {
        if (hasAiRun) return
        
        val ocrName = binding.edtProductName.text.toString()
        val ocrIngredients = binding.txtExtractedIngredients.text.toString()
        
        // Don't call if missing core data or both images
        if (productImageUri == null || capturedIngredientsUri == null) return
        if (ocrName.isBlank() && ocrIngredients.isBlank()) return

        hasAiRun = true
        binding.layoutScanning.visibility = View.VISIBLE
        
        callOpenAI(binding.edtBarcode.text.toString(), ocrName, ocrIngredients)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ aiResult ->
                binding.layoutScanning.visibility = View.GONE
                
                // ✅ UPDATE UI ONLY AFTER AI RESPONDS
                binding.edtProductName.setText(aiResult.optString("product_name", ocrName))
                binding.txtExtractedIngredients.text = aiResult.optString("ingredients", ocrIngredients)
                binding.edtCategory.setText(aiResult.optString("category", ""))
                binding.edtCountry.setText(aiResult.optString("country", ""))
                
                val status = aiResult.optString("halal_status", "").uppercase()
                aiDetectedStatus = status
                
                updateStatusUI(status, aiResult.optString("reason", ""))
                selectLabelFromAI(status)
                
                Toast.makeText(requireContext(), "✨ AI Optimized Product Details", Toast.LENGTH_SHORT).show()
                
            }, { error ->
                Log.e("AI", "Magic AI failed", error)
                binding.layoutScanning.visibility = View.GONE
                hasAiRun = false // Allow retry if it failed
            })
    }

    private fun updateStatusUI(status: String, reason: String) {
        binding.txtAIStatus.visibility = View.VISIBLE
        binding.txtAIStatus.text = "Status: $status\nReason: $reason"

        val cleanStatus = status.trim().uppercase()
        selectedHalalStatus = cleanStatus


        when(status) {
            "HALAL" -> binding.txtAIStatus.setBackgroundColor("#4CAF50".toColorInt())
            "HARAM" -> binding.txtAIStatus.setBackgroundColor("#F44336".toColorInt())
            "MUSHBOOH", "DOUBTFUL" -> binding.txtAIStatus.setBackgroundColor("#FFC107".toColorInt())
            else -> binding.txtAIStatus.setBackgroundColor(Color.GRAY)
        }
    }

    private fun selectLabelFromAI(status: String) {
        when(status) {
            "HALAL" -> selectLabel("Halal")
            "VEGAN" -> selectLabel("Vegan")
            "VEGETARIAN" -> selectLabel("Vegetarian")
            "KOSHER" -> selectLabel("Kosher")
        }
    }

    private fun callOpenAI(barcode: String, ocrName: String, ocrIngredients: String): Single<JSONObject> {
        return Single.create { emitter ->
            val client = OkHttpClient()
            val apiKey = BuildConfig.OPENAI_API_KEY

            val prompt = """
                Master Rule: You are a professional Halal Food Scientist and Data Entry Expert.
                
                Input Data:
                - Barcode: $barcode
                - OCR Product Name: $ocrName
                - OCR Ingredients: $ocrIngredients

                Instructions:
                1. REFINEMENT: The OCR text comes directly from the product images. Your primary job is to CLEAN and FIX typos in the OCR text.
                2. NO OVERWRITE: If the 'OCR Product Name' is clear and recognizable, DO NOT replace it with a different name. Only fix capitalization, punctuation, and minor spelling errors.
                3. INGREDIENTS: If 'OCR Ingredients' contains text, fix the spelling and formatting. Only 'guess' or 'fill in' missing ingredients if the text is clearly incomplete or unreadable. Treat the OCR text as the primary source of truth.
                4. ANALYSIS: Based on the refined data, determine:
                   - Category (e.g., Snacks, Beverages, Dairy)
                   - Country of Manufacturing (e.g., Australia, USA, Turkey)
                   - Halal Status (HALAL, HARAM, or MUSHBOOH)
                   - Reason: A short explanation of the status based on ingredients.

                Return ONLY a valid JSON object. No markdown, no explanation.
                
                JSON Format:
                {
                  "product_name": "string",
                  "ingredients": "string",
                  "category": "string",
                  "country": "string",
                  "halal_status": "string",
                  "reason": "string"
                }
            """.trimIndent()

            val contentArray = JSONArray().put(JSONObject().put("type", "text").put("text", prompt))
            val userMessage = JSONObject().put("role", "user").put("content", contentArray)
            val requestBodyJson = JSONObject()
                .put("model", "gpt-4o-mini")
                .put("messages", JSONArray().put(userMessage))
                .put("response_format", JSONObject().put("type", "json_object"))

            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), requestBodyJson.toString())
            val request = Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer $apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) { emitter.onError(e) }
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: ""
                        val jsonResponse = JSONObject(body)
                        val content = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content")
                        emitter.onSuccess(JSONObject(content))
                    } catch (e: Exception) { emitter.onError(e) }
                }
            })
        }
    }

    private fun selectLabel(label: String) {
        selectedLabel = label
        binding.btnHalal.alpha = if (label == "Halal") 1.0f else 0.5f
        binding.btnVegan.alpha = if (label == "Vegan") 1.0f else 0.5f
        binding.btnVegetarian.alpha = if (label == "Vegetarian") 1.0f else 0.5f
        binding.btnKosher.alpha = if (label == "Kosher") 1.0f else 0.5f
    }

    @SuppressLint("CheckResult")
    private fun submitProduct() {

        if (binding.edtProductName.text.isNullOrBlank()) {
            binding.edtProductName.error = "Required"
            return
        }

        if (productImageUri == null) {
            Toast.makeText(requireContext(), "Please select product image", Toast.LENGTH_SHORT).show()
            return
        }

        binding.layoutUploadProduct.visibility = View.GONE
        binding.layoutSubmitting.visibility = View.VISIBLE

        try {

            // ✅ Compress Image First
            val compressedFile = compressImage(productImageUri!!)

            val imageRequestBody =
                compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val imagePart = MultipartBody.Part.createFormData(
                "image",
                compressedFile.name,
                imageRequestBody
            )

            apiService.addProduct(
                barcode.toRequestBody(),
                binding.edtProductName.text.toString().toRequestBody(),
                selectedLabel.toRequestBody(),
                binding.edtCategory.text.toString().toRequestBody(),
                binding.edtCountry.text.toString().toRequestBody(),
                binding.txtExtractedIngredients.text.toString().toRequestBody(),
                selectedHalalStatus.toRequestBody(),
                imagePart
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    Toast.makeText(
                        requireContext(),
                        "Product Submitted ",
                        Toast.LENGTH_SHORT
                    ).show()
                    onProductAdded?.invoke()
                    dismiss()

                }, {

                    it.printStackTrace()

                    Toast.makeText(
                        requireContext(),
                        "Failed ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.layoutSubmitting.visibility = View.GONE
                    binding.layoutUploadProduct.visibility = View.VISIBLE
                })

        } catch (e: Exception) {

            e.printStackTrace()

            Toast.makeText(
                requireContext(),
                "Image Error ❌",
                Toast.LENGTH_SHORT
            ).show()

            binding.layoutSubmitting.visibility = View.GONE
            binding.layoutUploadProduct.visibility = View.VISIBLE
        }
    }
    private fun compressImage(uri: Uri): File {

        val bitmap = MediaStore.Images.Media.getBitmap(
            requireContext().contentResolver,
            uri
        )

        // ✅ Resize Large Images
        val resizedBitmap = bitmap.scale(1080, 1080)

        val file = File(
            requireContext().cacheDir,
            "product_${System.currentTimeMillis()}.jpg"
        )

        val outputStream = FileOutputStream(file)

        // ✅ Compress Quality (0 - 100)
        resizedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            70,
            outputStream
        )

        outputStream.flush()
        outputStream.close()

        return file
    }

    private fun String.toRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var path = ""
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val index = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            if (index != -1) {
                path = it.getString(index)
            }
        }
        if (path.isEmpty()) {
            path = uri.path ?: ""
        }
        return path
    }

    override fun onStart() {
        super.onStart()

        val bottomSheetDialog =
            dialog as? com.google.android.material.bottomsheet.BottomSheetDialog

        // Prevent dismiss on outside touch
        bottomSheetDialog?.setCanceledOnTouchOutside(false)

        // Prevent back press cancel
        isCancelable = false

        // Prevent swipe down dismiss
        val bottomSheet = bottomSheetDialog?.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        )

        bottomSheet?.let {
            val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(it)

            behavior.isDraggable = false
            behavior.state =
                com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun initApi() {
        apiService = Retrofit.Builder()
            .baseUrl("https://admin.halaladvisor.com.au/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}