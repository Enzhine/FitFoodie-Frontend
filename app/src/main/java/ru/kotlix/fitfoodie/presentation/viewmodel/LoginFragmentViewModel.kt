package ru.kotlix.fitfoodie.presentation.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.openapitools.client.infrastructure.ApiClient
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.api.dto.LoginRequest
import ru.kotlix.fitfoodie.domain.service.TokenStorage
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorage
import ru.kotlix.fitfoodie.domain.service.UserPreferencesStorage
import ru.kotlix.fitfoodie.mapper.toUserCredentials
import ru.kotlix.fitfoodie.mapper.toUserPreferences
import ru.kotlix.fitfoodie.presentation.state.LoginState
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val apiClient: ApiClient,
    private val defaultApi: DefaultApi,
    private val tokenStorage: TokenStorage,
    private val userCredentialsStorage: UserCredentialsStorage,
    private val userPreferencesStorage: UserPreferencesStorage
) : ViewModel() {
    private val hasSubmitted = MutableStateFlow(false)
    val loginState = MutableStateFlow<LoginState>(LoginState.Idle)

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    val emailError: StateFlow<String?> = combine(hasSubmitted, email) { submit, email ->
        if (!submit) null
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Неверный e-mail"
        else null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val passwordError: StateFlow<String?> = combine(hasSubmitted, password) { submit, pass ->
        if (!submit) null
        else if (pass.length < 6) "Минимум 6 символов"
        else null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isFormValid: StateFlow<Boolean> = combine(
        emailError, passwordError
    ) { ee, pe -> ee == null && pe == null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun login() {
        if (loginState.value == LoginState.Loading) return

        hasSubmitted.value = true
        if (!isFormValid.value) return
        loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val responseLogin = defaultApi.authLogin(
                    LoginRequest(
                        password = password.value,
                        email = email.value
                    )
                )
                if (!responseLogin.isSuccessful) {
                    throw IllegalArgumentException("TokenFetch: error ${responseLogin.code()}")
                }

                val responseLoginBody = responseLogin.body()
                    ?: throw IllegalArgumentException("TokenFetch: empty response")

                apiClient.setBearerToken(responseLoginBody.token)
                fetchCredentials()
                tokenStorage.save(responseLoginBody.token)

                loginState.value = LoginState.Success
            } catch (t: Throwable) {
                Log.e(this::class.toString(), t.message, t)
                loginState.value = LoginState.Error(t.message)
            }
        }
    }

    private suspend fun fetchCredentials() {
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
    }
}