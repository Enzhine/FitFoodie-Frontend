package ru.kotlix.fitfoodie.domain.dto

data class UserPreferences(
    val meat: ProductKind,
    val fish: ProductKind,
    val milk: ProductKind,
) {
    enum class ProductKind {
        LIKE,
        RARE,
        EXCL
    }
}
