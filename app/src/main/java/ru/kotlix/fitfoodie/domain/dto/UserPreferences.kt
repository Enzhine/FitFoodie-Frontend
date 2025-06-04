package ru.kotlix.fitfoodie.domain.dto

data class UserPreferences(
    var meat: ProductKind,
    var fish: ProductKind,
    var milk: ProductKind,
) {
    enum class ProductKind {
        LIKE,
        RARE,
        EXCL
    }
}
