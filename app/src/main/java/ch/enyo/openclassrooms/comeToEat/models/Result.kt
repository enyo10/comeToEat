package ch.enyo.openclassrooms.comeToEat.models

data class Result(
    val count: Int,
    val from: Int,
    val hits: List<Hit>,
    val more: Boolean,
    val q: String,
    val to: Int
)