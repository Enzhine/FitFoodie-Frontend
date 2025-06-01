package ru.kotlix.fitfoodie.domain.service

import kotlinx.coroutines.flow.Flow

interface TokenStorage {
    fun get(): Flow<String?>

    suspend fun save(token: String)

    suspend fun remove()
}