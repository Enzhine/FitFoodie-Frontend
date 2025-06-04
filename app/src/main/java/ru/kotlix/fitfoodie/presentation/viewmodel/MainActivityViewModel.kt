package ru.kotlix.fitfoodie.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import org.openapitools.client.infrastructure.ApiClient
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.domain.service.TokenStorage
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorage
import ru.kotlix.fitfoodie.domain.service.UserPreferencesStorage
import ru.kotlix.fitfoodie.mapper.toUserCredentials
import ru.kotlix.fitfoodie.mapper.toUserPreferences
import ru.kotlix.fitfoodie.presentation.state.PreauthState
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiClient: ApiClient,
    private val defaultApi: DefaultApi,
    private val tokenStorage: TokenStorage,
    private val userCredentialsStorage: UserCredentialsStorage,
    private val userPreferencesStorage: UserPreferencesStorage
) : ViewModel() {

    suspend fun tryFetchCredentials(): PreauthState {
        val token = tokenStorage.get().first()
            ?: return PreauthState.EMPTY

        try {
            apiClient.setBearerToken(token)

            val response = defaultApi.usersMe()
            if (!response.isSuccessful) {
                throw IllegalArgumentException("UserFetch: error ${response.code()}")
            }

            val responseBody = response.body()
                ?: throw IllegalArgumentException("UserFetch: empty response")

            val userCredentials = responseBody.toUserCredentials()
            val userPreferences = responseBody.toUserPreferences()

            userCredentialsStorage.save(userCredentials)
            userPreferencesStorage.save(userPreferences)

            return PreauthState.OK
        } catch (ex: Exception) {
            Log.e(this::class.toString(), ex.message, ex)
            return PreauthState.ERROR
        }
    }
}