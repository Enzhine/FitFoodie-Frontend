package ru.kotlix.fitfoodie.domain.service

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.domain.datastore
import ru.kotlix.fitfoodie.domain.dto.Product
import ru.kotlix.fitfoodie.mapper.toProduct
import javax.inject.Inject

class ProductStorageImpl @Inject constructor(
    @ApplicationContext
    private val ctx: Context,
    private val productsCacheHash: ProductsCacheHash,
    private val userCredentialsStorage: UserCredentialsStorage,
    private val defaultApi: DefaultApi,
    moshi: Moshi
) : ProductStorage {

    override var updated: MutableStateFlow<Boolean> = MutableStateFlow(true)

    private fun notifyUpdate() {
        updated.value = !updated.value
    }

    private val adapter = moshi.adapter(Product::class.java)

    private fun keyHash(userId: Int) =
        stringPreferencesKey("products_${userId}_hash")

    private fun keyIds(userId: Int) =
        stringSetPreferencesKey("products_${userId}")

    private fun keyProduct(userId: Int, productId: Int) =
        stringPreferencesKey("products_${userId}_${productId}")

    override suspend fun getAll(userId: Int?): List<Product>? {
        trySyncServer()

        val id = userId
            ?: userCredentialsStorage.get().first()?.id
            ?: throw IllegalStateException("UserCredentials do not present.")

        val keyIds = keyIds(id)
        val pids = ctx.datastore.data.map { it[keyIds] }.first()
            ?: return null

        return pids.mapNotNull { pidStr -> get(pidStr.toInt(), userId = id) }
    }

    override suspend fun getNonempty(userId: Int?): List<Product>? =
        getAll(userId)?.filter { it.quantity > 0 }

    override suspend fun get(pid: Int, userId: Int?): Product? {
        val id = userId
            ?: userCredentialsStorage.get().first()?.id
            ?: throw IllegalStateException("UserCredentials do not present.")

        val keyProduct = keyProduct(id, pid)
        val product = ctx.datastore.data.map { it[keyProduct] }.first()
            ?: return null

        return try {
            adapter.fromJson(product)
        } catch (ex: JsonDataException) {
            Log.e(this::class.toString(), "Unable to parse: $product")
            null
        }
    }

    override suspend fun save(product: Product, userId: Int?) {
        val id = userId
            ?: userCredentialsStorage.get().first()?.id
            ?: throw IllegalStateException("UserCredentials do not present.")

        val keyProduct = keyProduct(id, product.id)
        ctx.datastore.edit { prefs ->
            prefs[keyProduct] = adapter.toJson(product)
        }
        notifyUpdate()
    }

    override suspend fun merge(products: List<Product>, userId: Int?) {
        val id = userId
            ?: userCredentialsStorage.get().first()?.id
            ?: throw IllegalStateException("UserCredentials do not present.")

        val keyHash = keyHash(id)
        val hash = ctx.datastore.data.map { it[keyHash] }.first()
        val newHash = productsCacheHash.hash(products)
        if (hash != null && hash == newHash) {
            return
        }

        val keyIds = keyIds(id)
        products.forEach { new ->
            get(new.id, userId = id)?.let {
                new.quantity = new.quant * (it.quantity / new.quant)
            }
            save(new, userId = id)
        }
        ctx.datastore.edit { prefs -> prefs[keyIds] = products.map { it.id.toString() }.toSet() }
        ctx.datastore.edit { it[keyHash] = newHash }
    }

    override suspend fun remove(productId: Int, userId: Int?) {
        val id = userId
            ?: userCredentialsStorage.get().first()?.id
            ?: throw IllegalStateException("UserCredentials do not present.")

        TODO("Not yet implemented")
    }

    private var productsOnceLoaded = false

    private suspend fun trySyncServer() {
        if (!productsOnceLoaded) {
            try {
                val response = defaultApi.products()
                if (!response.isSuccessful) {
                    throw IllegalArgumentException("ProductsFetch: error ${response.code()}")
                }

                val responseBody = response.body()
                    ?: throw IllegalArgumentException("ProductsFetch: empty response.")

                merge(responseBody.map { it.toProduct() })
                productsOnceLoaded = true
            } catch (ex: Exception) {
                Log.e(this::class.toString(), ex.message, ex)
            }
        }
    }
}