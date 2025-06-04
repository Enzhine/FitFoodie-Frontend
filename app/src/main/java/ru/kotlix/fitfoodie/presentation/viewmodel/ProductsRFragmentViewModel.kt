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
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.domain.dto.Product
import ru.kotlix.fitfoodie.domain.service.ProductStorage
import ru.kotlix.fitfoodie.domain.service.UserRelativeTagsProvider
import ru.kotlix.fitfoodie.mapper.toProduct
import javax.inject.Inject

@HiltViewModel
open class ProductsRFragmentViewModel @Inject constructor(
    private val defaultApi: DefaultApi,
    private val productStorage: ProductStorage,
) : ViewModel() {

    var requiredProducts = MutableStateFlow<List<Product>?>(null)

    var nameFilter = MutableStateFlow("")

    val products: StateFlow<List<Product>> =
        combine(requiredProducts, nameFilter) { rp, name ->
            val products = rp ?: emptyList()

            products.filter { product -> product.title.contains(name) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    suspend fun getRequiredProducts(dishesId: List<Int>): List<Product> {
        val all = productStorage.getAll() ?: emptyList()
        val reqMap = mutableMapOf<Int, Int>()

        dishesId.forEach { dishId ->
            try {
                val response = defaultApi.dishProducts(dishId)
                if (!response.isSuccessful) {
                    throw IllegalArgumentException("DishProducts: error ${response.code()}")
                }

                val products = response.body() ?: emptyList()
                products.forEach { q ->
                    val current = reqMap[q.productId] ?: 0
                    reqMap[q.productId] = current + q.quantity
                }
            } catch (ex: Exception) {
                Log.e(this::class.toString(), ex.message, ex)
            }
        }

        return all.filter { reqMap.containsKey(it.id) }
            .map {
                it.apply {
                    requiredQuantity = reqMap[it.id]
                }
            }
    }
}
