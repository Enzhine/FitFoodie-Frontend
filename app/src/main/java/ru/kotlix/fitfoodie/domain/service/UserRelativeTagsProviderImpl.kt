package ru.kotlix.fitfoodie.domain.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import ru.kotlix.fitfoodie.domain.dto.CategoryTag
import ru.kotlix.fitfoodie.domain.dto.Product
import ru.kotlix.fitfoodie.domain.dto.UserPreferences
import javax.inject.Inject

class UserRelativeTagsProviderImpl @Inject constructor(
    @ApplicationContext
    private val ctx: Context,
    private val userPreferencesStorage: UserPreferencesStorage,
) : UserRelativeTagsProvider {

    private fun mapPreferencesToExcluded(up: UserPreferences): Set<CategoryTag> {
        val excl = mutableSetOf<CategoryTag>()
        if (up.meat == UserPreferences.ProductKind.EXCL) {
            excl.add(CategoryTag.MEAT)
        }
        if (up.fish == UserPreferences.ProductKind.EXCL) {
            excl.add(CategoryTag.FISH)
        }
        if (up.milk == UserPreferences.ProductKind.EXCL) {
            excl.add(CategoryTag.MILK)
        }
        return excl
    }

    override suspend fun getExcludedCategories(userId: Int?): Set<CategoryTag> {
        val prefs = userPreferencesStorage.get(userId).first()
            ?: throw IllegalStateException("UserPreferences does not present.")
        return mapPreferencesToExcluded(prefs)
    }
}
