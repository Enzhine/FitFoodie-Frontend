package ru.kotlix.fitfoodie.domain.dto

data class Product(
    val id: Int,
    var title: String,
    var quantity: Int,
    var quant: Int,
    var unit: Unit,
    var tags: Set<CategoryTag>,
    var requiredQuantity: Int? = null
) {
    enum class Unit {
        G,
        ML,
        ITEMS,
        TSP,
        TBSP
    }
}
