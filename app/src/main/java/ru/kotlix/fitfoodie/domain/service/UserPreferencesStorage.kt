package ru.kotlix.fitfoodie.domain.service

import kotlinx.coroutines.flow.Flow
import ru.kotlix.fitfoodie.domain.dto.UserPreferences

interface UserPreferencesStorage {
    fun get(userId: Int? = null): Flow<UserPreferences?>

    suspend fun save(userPreferences: UserPreferences, userId: Int? = null)

    suspend fun remove(userId: Int? = null)
}