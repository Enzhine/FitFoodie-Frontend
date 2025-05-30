package ru.kotlix.fitfoodie.presentation.state

sealed class RegistrationState {
    data object Idle : RegistrationState()
    data object Loading : RegistrationState()
    data object Success : RegistrationState()
    data class Error(val message: String?) : RegistrationState()
}