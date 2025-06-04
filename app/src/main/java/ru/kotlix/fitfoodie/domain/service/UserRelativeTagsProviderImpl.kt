package ru.kotlix.fitfoodie.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import ru.kotlix.fitfoodie.domain.dto.CategoryTag
import ru.kotlix.fitfoodie.domain.dto.UserPreferences
import javax.inject.Inject

class UserRelativeTagsProviderImpl @Inject constructor(
    private val userPreferencesStorage: UserPreferencesStorage,
) : UserRelativeTagsProvider {

    override var updated: Flow<Boolean> = userPreferencesStorage.updated

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
