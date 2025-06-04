package ru.kotlix.fitfoodie.domain.dto

import java.math.BigDecimal

data class DishFull(
    val id: Int,
    val title: String,
    val calories: BigDecimal,
    val cookMinutes: Int,
    val tags: Set<CategoryTag>,
    val order: Int,
    val props: Map<String, String>,
    var recipe: String,
    var chefAdvice: String?,
)
