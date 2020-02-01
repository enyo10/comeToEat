package ch.enyo.openclassrooms.comeToEat.models

data class Recipe(
    val calories: Double,
    val cautions: List<String>,
    val dietLabels: List<String>,
    val digest: List<Digest>,
    val healthLabels: List<String>,
    val image: String,
    val ingredientLines: List<String>,
    val ingredients: List<Ingredient>,
    val label: String,
    val shareAs: String,
    val source: String,
    val totalDaily: TotalDaily,
    val totalNutrients: TotalNutrients,
    val totalTime: Double,
    val totalWeight: Double,
    val uri: String,
    val url: String,
    val yield: Double
)