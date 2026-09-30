package com.dapl.halaladvisor.productscan

object FoodClassifier {

    // -----------------------------
    // DEFINITELY HARAM
    // -----------------------------

    private val haramIngredients = setOf(
        "pork", "ham", "bacon", "lard", "pepperoni", "pig", "porcine",
        "wine", "beer", "rum", "whisky", "vodka", "ethanol", "alcohol",
        "blood", "blood plasma",
        "carmine", "cochineal",
        "animal fat", "animal shortening",
        "shellac", "rennet", "animal enzyme", "l-cysteine",
        "porc", "jambon",
        "schwein", "schinken",
        "cerdo", "jamon",
        "gelatin from pork"
    )

    // -----------------------------
    // POSSIBLY HARAM / DOUBTFUL
    // -----------------------------

    private val doubtfulIngredients = setOf(
        "gelatin", "gelatine", "glycerin", "glycerol", "emulsifier", "enzyme",
        "mono and diglycerides", "diglycerides", "shortening", "flavour", "flavor",
        "flavours", "flavors", "natural flavour", "artificial flavour", "chicken", "crustacean"
    )

    // -----------------------------
    // DEFINITELY HARAM E NUMBERS
    // -----------------------------

    private val haramENumbers = setOf(
        "e120", // carmine
        "e441", // gelatin
        "e542"  // bone phosphate
    )

    // -----------------------------
    // DOUBTFUL E NUMBERS
    // -----------------------------

    private val doubtfulENumbers = setOf(
        "e471", "e472", "e473", "e474", "e475", "e476", "e477", "e631", "e627", "e635", "e640", "e920"
    )

    // -----------------------------
    // REGEX FOR E NUMBERS
    // -----------------------------

    private val eNumberRegex = Regex("\\be[- ]?\\d{3}\\b")

    // -----------------------------
    // CLASSIFIER
    // -----------------------------

    fun classify(
        text: String?,
        productName: String = "",
        brand: String = ""
    ): Result {

        val combinedText =
            "$productName $brand ${text ?: ""}".lowercase()

        // -----------------------------
        // TRUSTED HALAL PRODUCTS
        // -----------------------------

        val trustedHalalBrands = listOf(
            "pepsi",
            "coca cola",
            "coca-cola",
            "sprite",
            "fanta",
            "7up",
            "mountain dew",
            "mirinda",
            "coconut water",
            "100% coconut water",
            "pure coconut water",
            "cocunut water",
            "100% cocunut water",
            "pure cocunut water"
        )

        if (trustedHalalBrands.any {
                combinedText.contains(it)
            }) {

            return Result(
                "HALAL",
                "Verified halal beverage or pure ingredient"
            )
        }

        // -----------------------------
        // NO INGREDIENTS
        // -----------------------------

        if (text.isNullOrBlank()) {
            return Result(
                "MUSHBOOH",
                "Ingredients not available"
            )
        }

        val normalized = text.lowercase()

        // -----------------------------
        // E NUMBERS
        // -----------------------------

        val foundENumbers = eNumberRegex.findAll(normalized)
            .map {
                it.value.replace("-", "")
                    .replace(" ", "")
            }
            .toList()

        val haramE = foundENumbers.filter {
            haramENumbers.contains(it)
        }

        if (haramE.isNotEmpty()) {
            return Result(
                "HARAM",
                "Haram additive detected",
                haramE
            )
        }

        val doubtfulE = foundENumbers.filter {
            doubtfulENumbers.contains(it)
        }

        // -----------------------------
        // HARAM INGREDIENTS
        // -----------------------------

        val haramIng = haramIngredients.filter {
            Regex("\\b$it\\b").containsMatchIn(normalized)
        }

        if (haramIng.isNotEmpty()) {
            return Result(
                "HARAM",
                "Haram ingredient detected",
                haramIng
            )
        }

        // -----------------------------
        // VERIFIED HALAL BRANDS
        // -----------------------------

        if (normalized.contains("the natural confectionery co")) {
            return Result(
                "HALAL",
                "Verified halal confectionery brand"
            )
        }

        // -----------------------------
        // DOUBTFUL INGREDIENTS
        // -----------------------------

        val foundDoubtful = doubtfulIngredients.filter {
            Regex("\\b$it\\b").containsMatchIn(normalized)
        }.toMutableList()

        if (normalized.contains("gelatin") ||
            normalized.contains("gelatine")
        ) {

            if (!foundDoubtful.contains("gelatin")) {
                foundDoubtful.add("gelatin")
            }
        }

        if (
            normalized.contains("cheese") ||
            normalized.contains("cheddar") ||
            normalized.contains("mozzarella") ||
            normalized.contains("feta")
        ) {
            foundDoubtful.add("enzyme")
        }

        // -----------------------------
        // FINAL DOUBTFUL CHECK
        // -----------------------------

        if (doubtfulE.isNotEmpty() ||
            foundDoubtful.isNotEmpty()
        ) {

            val allDoubtful =
                (doubtfulE + foundDoubtful).distinct()

            return Result(
                "MUSHBOOH",
                "Possible animal derived ingredient",
                allDoubtful
            )
        }

        // -----------------------------
        // SAFE
        // -----------------------------

        return Result(
            "HALAL",
            "No haram ingredient detected"
        )
    }
    data class Result(
        val halalStatus: String, 
        val reason: String,
        val triggers: List<String> = emptyList()
    )

    fun guessIngredients(productName: String): String {
        val name = productName.lowercase()
        return when {
            name.contains("candy") || name.contains("jelly") || name.contains("gummy") || 
            name.contains("lolly") || name.contains("sweet") || name.contains("mix") ->
                "Glucose syrup, Cane sugar, Gelatin, Citric acid, Food flavour, Food colour"
            name.contains("biscuit") || name.contains("cookie") ->
                "Wheat flour, Sugar, Vegetable oil, Milk powder, Raising agent, Flavour"
            name.contains("chocolate") ->
                "Cocoa mass, Cocoa butter, Sugar, Milk powder, Emulsifier E322, Flavour"
            name.contains("chips") || name.contains("crisps") ->
                "Potato, Vegetable oil, Salt, Flavour"
            name.contains("drink") || name.contains("cola") || name.contains("soda") ->
                "Water, Sugar, Carbon dioxide, Citric acid, Flavour, Colour"
            else -> ""
        }
    }
}
