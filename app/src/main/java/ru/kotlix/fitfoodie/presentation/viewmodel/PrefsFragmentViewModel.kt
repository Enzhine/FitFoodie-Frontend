package ru.kotlix.fitfoodie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.api.dto.PreferencesUpdate
import ru.kotlix.fitfoodie.domain.dto.UserPreferences
import ru.kotlix.fitfoodie.domain.service.TokenStorage
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorage
import ru.kotlix.fitfoodie.domain.service.UserPreferencesStorage
import ru.kotlix.fitfoodie.mapper.toPreferencesUpdate
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
        userPreferencesStorage.remove()
    }

    val meatPreference: Flow<UserPreferences.ProductKind?> =
        userPreferencesStorage.get().map { it?.meat }

    suspend fun setMeatPreference(pref: UserPreferences.ProductKind) {
        val userPrefs = userPreferencesStorage.get().first()
            ?: return
        userPrefs.meat = pref
        userPreferencesStorage.save(userPrefs)
    }

    val fishPreference: Flow<UserPreferences.ProductKind?> =
        userPreferencesStorage.get().map { it?.fish }

    suspend fun setFishPreference(pref: UserPreferences.ProductKind) {
        val userPrefs = userPreferencesStorage.get().first()
            ?: return
        userPrefs.fish = pref
        userPreferencesStorage.save(userPrefs)
    }

    val milkPreference: Flow<UserPreferences.ProductKind?> =
        userPreferencesStorage.get().map { it?.milk }

    suspend fun setMilkPreference(pref: UserPreferences.ProductKind) {
        val userPrefs = userPreferencesStorage.get().first()
            ?: return
        userPrefs.milk = pref
        userPreferencesStorage.save(userPrefs)
    }

    fun syncPreferences() {
        viewModelScope.launch {
            val userPrefs = userPreferencesStorage.get().first()
                ?: return@launch

            defaultApi.usersPreferences(userPrefs.toPreferencesUpdate())
        }
    }
}
