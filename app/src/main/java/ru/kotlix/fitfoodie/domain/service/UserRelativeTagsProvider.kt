package ru.kotlix.fitfoodie.domain.service

import kotlinx.coroutines.flow.Flow
import ru.kotlix.fitfoodie.domain.dto.CategoryTag

interface UserRelativeTagsProvider {
    val updated: Flow<Boolean>

    suspend fun getExcludedCategories(userId: Int? = null): Set<CategoryTag>
}