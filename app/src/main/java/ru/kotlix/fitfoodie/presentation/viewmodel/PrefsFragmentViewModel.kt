package ru.kotlix.fitfoodie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.domain.service.TokenStorage
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorage
import ru.kotlix.fitfoodie.domain.service.UserPreferencesStorage
import javax.inject.Inject

@HiltViewModel
class PrefsFragmentViewModel @Inject constructor(
    private val defaultApi: DefaultApi,
    private val tokenStorage: TokenStorage,
    private val userPreferencesStorage: UserPreferencesStorage,
    private val userCredentialsStorage: UserCredentialsStorage
) : ViewModel() {

    val userName: Flow<String?> = userCredentialsStorage.get().map { it?.username }

    suspend fun resetCredentials() {
        tokenStorage.remove()
        val userCredentials = userCredentialsStorage.get().first()
        userCredentials?.let {
            userPreferencesStorage.remove(it.id)
        }
    }

}
