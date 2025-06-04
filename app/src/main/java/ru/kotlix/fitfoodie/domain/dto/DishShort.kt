package ru.kotlix.fitfoodie.domain.dto

import java.math.BigDecimal

data class DishShort(
    val id: Int,
    val title: String,
    val calories: BigDecimal,
    val cookMinutes: Int,
    val tags: Set<CategoryTag>,
    val order: Int,
    var selected: Boolean = false
)
