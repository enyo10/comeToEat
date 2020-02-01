package ch.enyo.openclassrooms.comeToEat.models

data class Hit(
    val bookmarked: Boolean,
    val bought: Boolean,
    val recipe: Recipe
)