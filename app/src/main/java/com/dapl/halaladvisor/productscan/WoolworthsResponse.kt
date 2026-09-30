package com.dapl.halaladvisor.productscan

data class WoolworthsResponse(
    val Products: List<ProductGroup>?
)

data class ProductGroup(
    val Products: List<WoolworthsProduct>?
)

data class WoolworthsProduct(
    val DisplayName: String?,
    val Brand: String?,
    val LargeImageFile: String?,
    val AdditionalAttributes: AdditionalAttributes?
)

data class AdditionalAttributes(
    val ingredients: String?,
    val sapcategoryname: String?,
    val countryoforigin: String?,
    val lifestyleclaim: String?,
    val allergencontains: String?,
    val allergenmaycontain: String?,
    val allergenmaybepresent: String?, // <- this exact name
    val productimages: String?,         // <- needed for OCR
    val productimagecount: String?
)