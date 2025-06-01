package ru.kotlix.fitfoodie.mapper

import ru.kotlix.fitfoodie.api.dto.UserDto
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
