package ru.kotlix.fitfoodie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.openapitools.client.infrastructure.ApiClient
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.domain.service.TokenStorage
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorage
import ru.kotlix.fitfoodie.domain.service.UserPreferencesStorage
import javax.inject.Inject

@HiltViewModel
class ProductsFragmentViewModel @Inject constructor(
    private val defaultApi: DefaultApi,
) : ViewModel() {

}
