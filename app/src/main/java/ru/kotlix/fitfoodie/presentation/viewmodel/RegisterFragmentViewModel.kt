package ru.kotlix.fitfoodie.presentation.viewmodel

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
import ru.kotlix.fitfoodie.presentation.state.RegistrationState
import javax.inject.Inject

@HiltViewModel
class RegisterFragmentViewModel @Inject constructor(
    private val defaultApi: DefaultApi
) : ViewModel() {

    val regState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    val nameError: StateFlow<String?> = name
        .map {
            if (it.isBlank()) "Имя не может быть пустое"
            else if (it.length < 2) "Имя не может быть короче двух букв"
            else if (it.length > 32) "Имя не может быть длинне 32 букв"
            else null
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val emailError: StateFlow<String?> = email
        .map { if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) "Неверный e-mail" else null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val passwordError: StateFlow<String?> = password
        .map { if (it.length < 6) "Минимум 6 символов" else null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isFormValid: StateFlow<Boolean> = combine(
        nameError, emailError, passwordError
    ) { ne, ee, pe -> ne == null && ee == null && pe == null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun register() {
        viewModelScope.launch {
            if (!isFormValid.value) return@launch

            regState.value = RegistrationState.Loading
            try {
                val response = defaultApi.authRegisterPost(
                    RegisterRequest(
                        username = name.value,
                        email = email.value,
                        password = password.value
                    )
                )
                if (response.isSuccessful) {
                    regState.value = RegistrationState.Success
                }else {
                    regState.value = RegistrationState.Error("Error ${response.code()}")
                }
            } catch (t: Throwable) {
                regState.value = RegistrationState.Error(t.message)
            }
        }
    }
}