package ru.kotlix.fitfoodie.domain.service

import kotlinx.coroutines.flow.Flow
import ru.kotlix.fitfoodie.domain.dto.Product

interface ProductStorage {
    val updated: Flow<Boolean>

    suspend fun getAll(userId: Int? = null): List<Product>?

    suspend fun getNonempty(userId: Int? = null): List<Product>?

    suspend fun get(pid: Int, userId: Int? = null): Product?

    suspend fun save(product: Product, userId: Int? = null)

    suspend fun merge(products: List<Product>, userId: Int? = null)

    suspend fun remove(productId: Int, userId: Int? = null)
}
