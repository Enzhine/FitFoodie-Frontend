package ru.kotlix.fitfoodie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorage
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val userCredentialsStorage: UserCredentialsStorage,
) : ViewModel() {
    val userName: Flow<String?> = userCredentialsStorage.get().map { it?.username }

}