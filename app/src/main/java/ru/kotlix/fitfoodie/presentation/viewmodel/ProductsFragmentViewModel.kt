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
open class ProductsFragmentViewModel @Inject constructor(
    private val userRelativeTagsProvider: UserRelativeTagsProvider,
    private val productStorage: ProductStorage,
) : ViewModel() {

    var nameFilter = MutableStateFlow("")

    val products: StateFlow<List<Product>> =
        combine(productStorage.updated, nameFilter) { _, name ->
            val excl = userRelativeTagsProvider.getExcludedCategories()

            getAllProducts().filter { product ->
                product.tags.all { tag -> !excl.contains(tag) } && product.title.contains(name)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    private suspend fun getAllProducts(): List<Product> {
        return productStorage.getAll() ?: emptyList()
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch { productStorage.save(product) }
    }
}
