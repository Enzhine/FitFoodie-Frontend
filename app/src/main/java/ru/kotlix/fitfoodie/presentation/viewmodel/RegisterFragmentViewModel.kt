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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.api.dto.RegisterRequest
import ru.kotlix.fitfoodie.presentation.state.LoginState
import ru.kotlix.fitfoodie.presentation.state.RegistrationState
import javax.inject.Inject

@HiltViewModel
class RegisterFragmentViewModel @Inject constructor(
    private val defaultApi: DefaultApi
) : ViewModel() {

    private val hasSubmitted = MutableStateFlow(false)
    val regState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    val nameError: StateFlow<String?> = combine(hasSubmitted, name) { submit, name ->
        if (!submit) null
        else if (name.isBlank()) "Имя не может быть пустое"
        else if (!Regex("^[0-9\\p{L}]+$").matches(name)) "Имя может содержать только буквы и цифры"
        else if (name.length < 2) "Имя не может быть короче двух букв"
        else if (name.length > 32) "Имя не может быть длинне 32 букв"
        else null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
        nameError, emailError, passwordError
    ) { ne, ee, pe -> ne == null && ee == null && pe == null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun register() {
        if (regState.value == RegistrationState.Loading) return

        hasSubmitted.value = true
        if (!isFormValid.value) return
        regState.value = RegistrationState.Loading

        viewModelScope.launch {
            try {
                val response = defaultApi.authRegister(
                    RegisterRequest(
                        username = name.value,
                        email = email.value,
                        password = password.value
                    )
                )
                if (response.isSuccessful) {
                    regState.value = RegistrationState.Success
                } else {
                    regState.value = RegistrationState.Error("Error ${response.code()}")
                }
            } catch (t: Throwable) {
                Log.e(this::class.toString(), t.message, t)
                regState.value = RegistrationState.Error(t.message)
            }
        }
    }
}