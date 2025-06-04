package ru.kotlix.fitfoodie.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.api.dto.ProductQuantityDto
import ru.kotlix.fitfoodie.domain.dto.DishShort
import ru.kotlix.fitfoodie.domain.service.ProductStorage
import ru.kotlix.fitfoodie.mapper.toDishShort
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
open class DishesFragmentViewModel @Inject constructor(
    private val defaultApi: DefaultApi,
    private val productStorage: ProductStorage
) : ViewModel() {

    var dishesRequested = MutableStateFlow<List<DishShort>?>(null)

    var nameFilter = MutableStateFlow("")

    var cookMinutesFilter = MutableStateFlow<Int?>(null)

    var caloriesFilter = MutableStateFlow<BigDecimal?>(null)

    val dishes: StateFlow<List<DishShort>> =
        combine(
            dishesRequested,
            nameFilter,
            cookMinutesFilter,
            caloriesFilter
        ) { req, name, cook, cal ->
            var dishes = req ?: emptyList()
            name.takeIf { it.isNotEmpty() }?.let {
                dishes = dishes.filter { it.title.contains(name) }
            }
            cook?.let {
                dishes = dishes.filter { it.cookMinutes <= cook }
            }
            cal?.let {
                dishes = dishes.filter { it.calories <= cal }
            }
            dishes
        }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    suspend fun requestDishes() {
        dishesRequested.value = findDishes()
    }

    private suspend fun findDishes(): List<DishShort>? {
        val contains = productStorage.getNonempty() ?: return null

        try {
            val response =
                defaultApi.dishesSuggest(contains.map { ProductQuantityDto(it.id, it.quantity) })

            if (!response.isSuccessful) {
                return null
            }

            return response.body()?.map { it.toDishShort() }
        } catch (ex: Exception) {
            Log.e(this::class.toString(), ex.message, ex)
            return null
        }
    }
}
