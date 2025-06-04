package ru.kotlix.fitfoodie.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.domain.dto.DishFull
import ru.kotlix.fitfoodie.mapper.toDishFull
import javax.inject.Inject

@HiltViewModel
open class DishFragmentViewModel @Inject constructor(
    private val defaultApi: DefaultApi,
) : ViewModel() {

    suspend fun getDish(id: Int): DishFull? {
        try {
            val response = defaultApi.dish(id)

            if (!response.isSuccessful) {
                return null
            }

            return response.body()?.toDishFull()
        } catch (ex: Exception) {
            Log.e(this::class.toString(), ex.message, ex)
            return null
        }
    }
}
