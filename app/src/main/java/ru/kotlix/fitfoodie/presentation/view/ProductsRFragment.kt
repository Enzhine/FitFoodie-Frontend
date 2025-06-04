package ru.kotlix.fitfoodie.presentation.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentProductsRequiredBinding
import ru.kotlix.fitfoodie.presentation.adapter.ProductRAdapter
import ru.kotlix.fitfoodie.presentation.viewmodel.ProductsRFragmentViewModel

@AndroidEntryPoint
class ProductsRFragment : Fragment(R.layout.fragment_products_required) {
    private var _b: FragmentProductsRequiredBinding? = null
    private val b get() = _b!!

    private val vm: ProductsRFragmentViewModel by viewModels()
    private val args: ProductsRFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentProductsRequiredBinding.bind(view)

        b.blockingOverlay.visibility = View.VISIBLE

        b.productsToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    showInputDialog()
                    true
                }

                else -> false
            }
        }

        b.productsList.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            vm.requiredProducts.value = vm.getRequiredProducts(args.id.toList())

            vm.products.collect {
                b.blockingOverlay.visibility = View.GONE

                b.productsList.adapter = ProductRAdapter(it)
            }
        }

    }

    private fun showInputDialog() {
        val ctx = requireContext()

        val input = EditText(ctx).apply {
            setText(vm.nameFilter.value)
            hint = getString(R.string.search_products_hint)
        }

        AlertDialog.Builder(ctx)
            .setTitle(getString(R.string.search_title))
            .setView(input)
            .setPositiveButton(getString(R.string.btn_search)) { _, _ ->
                vm.nameFilter.value = input.text.toString()
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _b = null
    }
}
