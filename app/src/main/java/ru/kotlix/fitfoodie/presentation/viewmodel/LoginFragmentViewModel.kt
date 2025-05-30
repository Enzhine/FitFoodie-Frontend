package ru.kotlix.fitfoodie.presentation.viewmodel

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
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.api.dto.LoginRequest
import ru.kotlix.fitfoodie.api.dto.RegisterRequest
import ru.kotlix.fitfoodie.presentation.state.LoginState
import ru.kotlix.fitfoodie.presentation.state.RegistrationState
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val defaultApi: DefaultApi
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
                val response = defaultApi.authLoginPost(
                    LoginRequest(
                        password = password.value,
                        login = email.value
                    )
                )
                if (response.isSuccessful) {
                    loginState.value = LoginState.Success
                } else {
                    loginState.value = LoginState.Error("Error ${response.code()}")
                }
            } catch (t: Throwable) {
                loginState.value = LoginState.Error(t.message)
            }
        }
    }
}