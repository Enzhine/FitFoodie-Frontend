package ru.kotlix.fitfoodie.domain.service

import ru.kotlix.fitfoodie.domain.dto.CategoryTag

interface UserRelativeTagsProvider {
    suspend fun getExcludedCategories(userId: Int? = null): Set<CategoryTag>
}