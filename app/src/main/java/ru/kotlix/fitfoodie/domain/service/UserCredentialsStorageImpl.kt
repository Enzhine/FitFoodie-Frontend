package ru.kotlix.fitfoodie.domain.service

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kotlix.fitfoodie.domain.datastore
import ru.kotlix.fitfoodie.domain.dto.UserCredentials
import javax.inject.Inject

class UserCredentialsStorageImpl @Inject constructor(
    @ApplicationContext
    private val ctx: Context
) : UserCredentialsStorage {

    private val keyEmail = stringPreferencesKey("creds_email")
    private val keyUsername = stringPreferencesKey("creds_username")
    private val keyId = intPreferencesKey("creds_id")

    override fun get(): Flow<UserCredentials?> =
        ctx.datastore.data.map { prefs ->
            val email = prefs[keyEmail] ?: return@map null
            val username = prefs[keyUsername] ?: return@map null
            val id = prefs[keyId] ?: return@map null

            UserCredentials(email, username, id)
        }

    override suspend fun save(userCredentials: UserCredentials) {
        ctx.datastore.edit { prefs ->
            prefs[keyEmail] = userCredentials.email
            prefs[keyUsername] = userCredentials.username
            prefs[keyId] = userCredentials.id
        }
    }

    override suspend fun remove() {
        ctx.datastore.edit { prefs ->
            prefs.remove(keyEmail)
            prefs.remove(keyUsername)
            prefs.remove(keyId)
        }
    }
}