package ru.kotlix.fitfoodie.domain.service

import kotlinx.coroutines.flow.Flow
import ru.kotlix.fitfoodie.domain.dto.UserPreferences

interface UserPreferencesStorage {
    fun get(id: Int): Flow<UserPreferences?>

    suspend fun save(id: Int, userPreferences: UserPreferences)

    suspend fun remove(id: Int)
}