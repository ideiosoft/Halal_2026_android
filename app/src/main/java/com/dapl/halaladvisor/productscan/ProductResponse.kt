//package com.dapl.halaladvisor.productscan
//
//data class ProductResponse(
//    val code: String?,
//    val status: Int?,
//    val product: Product?
//)
//data class Product(
//
//    val product_name: String?,
//    val brands: String?,
//    val quantity: String?,
//
//    val image_front_url: String?,
//    val image_ingredients_url: String?,
//    val image_nutrition_url: String?,
//
//    val ingredients_text: String?,
//
//    val additives_tags: List<String>?,
//
//    val ingredients: List<Ingredient>?,
//
//    val ingredients_analysis: IngredientsAnalysis?
//
//)
//
//data class Ingredient(
//
//    val id: String?,
//    val text: String?,
//    val vegan: String?,
//    val vegetarian: String?
//
//)
//
//data class IngredientsAnalysis(
//
//    val en: List<String>?,
//    val non: List<String>?,
//    val palmoil: List<String>?
//
//)


package com.dapl.halaladvisor.productscan

data class GoUpcResponse(
    val code: String?,
    val codeType: String?,
    val product: GoUpcProduct?,
    val inferred: Boolean?
)

data class GoUpcProduct(
    val name: String?,
    val description: String?,
    val brand: String?,
    val imageUrl: String?,
    val category: String?,
    val categoryPath: List<String>?,
    val specs: List<List<String>>?,   // raw from API
    val ingredients: IngredientsWrapper?,

    val ean: Long?,
    val upc: String?
)

data class IngredientsWrapper(
    val text: String?
)



data class ProductResponse(
    val code: String?,
    val status: Int?,
    val product: Product?
)
data class Product(

    val product_name: String?,
    val brands: String?,
    val quantity: String?,

    val image_front_url: String?,
    val image_ingredients_url: String?,
    val image_nutrition_url: String?,

    val ingredients_text: String?,

    val additives_tags: List<String>?,

    val ingredients: List<Ingredient>?,

    val ingredients_analysis: IngredientsAnalysis?,
    val countries: String?,
    val categories: String?


)

data class Ingredient(

    val id: String?,
    val text: String?,
    val vegan: String?,
    val vegetarian: String?

)

data class IngredientsAnalysis(

    val en: List<String>?,
    val non: List<String>?,
    val palmoil: List<String>?

)

data class SearchResponse(
    val products: List<Product>?
)
