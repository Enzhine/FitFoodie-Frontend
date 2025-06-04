package ru.kotlix.fitfoodie.mapper

import android.content.Context
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.api.dto.DishDtoFull
import ru.kotlix.fitfoodie.api.dto.DishDtoShort
import ru.kotlix.fitfoodie.api.dto.PreferencesUpdate
import ru.kotlix.fitfoodie.api.dto.ProductDto
import ru.kotlix.fitfoodie.api.dto.UserDto
import ru.kotlix.fitfoodie.domain.dto.CategoryTag
import ru.kotlix.fitfoodie.domain.dto.DishFull
import ru.kotlix.fitfoodie.domain.dto.DishShort
import ru.kotlix.fitfoodie.domain.dto.Product
import ru.kotlix.fitfoodie.domain.dto.UserCredentials
import ru.kotlix.fitfoodie.domain.dto.UserPreferences

fun UserDto.toUserCredentials() =
    UserCredentials(
        email = email,
        username = username,
        id = id
    )

fun UserDto.toUserPreferences() =
    UserPreferences(
        meat = UserPreferences.ProductKind.valueOf(meatPreference.value),
        fish = UserPreferences.ProductKind.valueOf(fishPreference.value),
        milk = UserPreferences.ProductKind.valueOf(milkPreference.value)
    )

fun UserPreferences.toPreferencesUpdate() =
    PreferencesUpdate(
        meatPreference = PreferencesUpdate.MeatPreference.valueOf(meat.name),
        fishPreference = PreferencesUpdate.FishPreference.valueOf(fish.name),
        milkPreference = PreferencesUpdate.MilkPreference.valueOf(milk.name)
    )

fun ProductDto.toProduct() =
    Product(
        id = id,
        title = name,
        quantity = 0,
        quant = quant,
        unit = unit.toProductUnit(),
        tags = tags.mapNotNull { tryOrNull { CategoryTag.valueOf(it) } }.toSet()
    )

private fun ProductDto.Unit.toProductUnit() = when (this) {
    ProductDto.Unit.g -> Product.Unit.G
    ProductDto.Unit.ml -> Product.Unit.ML
    ProductDto.Unit.items -> Product.Unit.ITEMS
    ProductDto.Unit.tsp -> Product.Unit.TSP
    ProductDto.Unit.tbsp -> Product.Unit.TBSP
}

inline fun <R> tryOrNull(block: () -> R): R? =
    try {
        block()
    } catch (_: Throwable) {
        null
    }

fun DishDtoShort.toDishShort() =
    DishShort(
        id = id,
        title = name,
        calories = calories,
        cookMinutes = cookMinutes,
        tags = tags.mapNotNull { tryOrNull { CategoryTag.valueOf(it) } }.toSet(),
        order = order
    )

fun DishDtoFull.toDishFull() =
    DishFull(
        id = id,
        title = name,
        calories = calories,
        cookMinutes = cookMinutes,
        tags = tags.mapNotNull { tryOrNull { CategoryTag.valueOf(it) } }.toSet(),
        order = order,
        props = props.associate { it.name to it.value },
        recipe = recipe,
        chefAdvice = chefAdvice
    )

fun Product.Unit.toLocalizedName(ctx: Context): String = when (this) {
    Product.Unit.G -> ctx.getString(R.string.unit_g)
    Product.Unit.ML -> ctx.getString(R.string.unit_ml)
    Product.Unit.ITEMS -> ctx.getString(R.string.unit_items)
    Product.Unit.TSP -> ctx.getString(R.string.unit_tsp)
    Product.Unit.TBSP -> ctx.getString(R.string.unit_tbsp)
}

fun CategoryTag.toLocalizedName(ctx: Context): String = when (this) {
    CategoryTag.MEAT -> ctx.getString(R.string.category_meat)
    CategoryTag.FISH -> ctx.getString(R.string.category_fish)
    CategoryTag.MILK -> ctx.getString(R.string.category_milk)
}
