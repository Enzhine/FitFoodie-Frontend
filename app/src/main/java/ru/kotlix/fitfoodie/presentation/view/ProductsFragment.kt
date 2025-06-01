package ru.kotlix.fitfoodie.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentProductsBinding
import ru.kotlix.fitfoodie.presentation.viewmodel.ProductsFragmentViewModel

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {
    private var _b: FragmentProductsBinding? = null
    private val b get() = _b!!

    private val vm: ProductsFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentProductsBinding.bind(view)
    }
}
