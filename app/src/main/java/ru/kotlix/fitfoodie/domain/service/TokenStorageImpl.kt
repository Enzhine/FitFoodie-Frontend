package ru.kotlix.fitfoodie.domain.service

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kotlix.fitfoodie.domain.datastore
import javax.inject.Inject

class TokenStorageImpl @Inject constructor(
    @ApplicationContext
    private val ctx: Context
) : TokenStorage {

    private val keyToken = stringPreferencesKey("token")

    override fun get(): Flow<String?> =
        ctx.datastore.data.map { prefs -> prefs[keyToken] }

    override suspend fun save(token: String) {
        ctx.datastore.edit { prefs ->
            prefs[keyToken] = token
        }
    }

    override suspend fun remove() {
        ctx.datastore.edit { prefs ->
            prefs.remove(keyToken)
        }
    }
}