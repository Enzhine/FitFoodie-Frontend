package ru.kotlix.fitfoodie.domain.service

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kotlix.fitfoodie.domain.datastore
import ru.kotlix.fitfoodie.domain.dto.UserPreferences
import javax.inject.Inject

class UserPreferencesStorageImpl @Inject constructor(
    @ApplicationContext
    private val ctx: Context
) : UserPreferencesStorage {

    private fun keyMeat(id: Int) =
        stringPreferencesKey("prefs_${id}_meat")

    private fun keyFish(id: Int) =
        stringPreferencesKey("prefs_${id}_fish")

    private fun keyMilk(id: Int) =
        stringPreferencesKey("prefs_${id}_milk")

    override fun get(id: Int): Flow<UserPreferences?> {
        val keyMeat = keyMeat(id)
        val keyFish = keyFish(id)
        val keyMilk = keyMilk(id)

        return ctx.datastore.data.map { prefs ->
            val meat = prefs[keyMeat] ?: return@map null
            val fish = prefs[keyFish] ?: return@map null
            val milk = prefs[keyMilk] ?: return@map null

            UserPreferences(
                meat = UserPreferences.ProductKind.valueOf(meat),
                fish = UserPreferences.ProductKind.valueOf(fish),
                milk = UserPreferences.ProductKind.valueOf(milk),
            )
        }
    }

    override suspend fun save(id: Int, userPreferences: UserPreferences) {
        val keyMeat = keyMeat(id)
        val keyFish = keyFish(id)
        val keyMilk = keyMilk(id)

        ctx.datastore.edit { prefs ->
            prefs[keyMeat] = userPreferences.meat.name
            prefs[keyFish] = userPreferences.fish.name
            prefs[keyMilk] = userPreferences.milk.name
        }
    }

    override suspend fun remove(id: Int) {
        val keyMeat = keyMeat(id)
        val keyFish = keyFish(id)
        val keyMilk = keyMilk(id)

        ctx.datastore.edit { prefs ->
            prefs.remove(keyMeat)
            prefs.remove(keyFish)
            prefs.remove(keyMilk)
        }
    }
}