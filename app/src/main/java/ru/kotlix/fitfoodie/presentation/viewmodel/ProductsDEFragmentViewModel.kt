package ru.kotlix.fitfoodie.presentation.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kotlix.fitfoodie.domain.service.ProductStorage
import ru.kotlix.fitfoodie.domain.service.UserRelativeTagsProvider
import javax.inject.Inject

@HiltViewModel
class ProductsDEFragmentViewModel @Inject constructor(
    userRelativeTagsProvider: UserRelativeTagsProvider,
    productStorage: ProductStorage,
) : ProductsFragmentViewModel(userRelativeTagsProvider, productStorage)
