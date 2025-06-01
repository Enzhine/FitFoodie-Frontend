package ru.kotlix.fitfoodie.domain.service

import kotlinx.coroutines.flow.Flow
import ru.kotlix.fitfoodie.domain.dto.UserCredentials

interface UserCredentialsStorage {
    fun get(): Flow<UserCredentials?>

    suspend fun save(userCredentials: UserCredentials)

    suspend fun remove()
}