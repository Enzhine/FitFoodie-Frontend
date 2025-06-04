package ru.kotlix.fitfoodie.presentation.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentProductsDishesEvalBinding
import ru.kotlix.fitfoodie.presentation.adapter.ProductAdapter
import ru.kotlix.fitfoodie.presentation.viewmodel.ProductsDEFragmentViewModel

@AndroidEntryPoint
class ProductsDEFragment : Fragment(R.layout.fragment_products_dishes_eval) {
    private var _b: FragmentProductsDishesEvalBinding? = null
    private val b get() = _b!!

    private val vm: ProductsDEFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentProductsDishesEvalBinding.bind(view)

        b.included.blockingOverlay.visibility = View.VISIBLE

        b.included.productsToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    showInputDialog()
                    true
                }

                else -> false
            }
        }

        b.included.productsList.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            vm.products.collect {
                b.included.blockingOverlay.visibility = View.GONE

                b.included.productsList.adapter = ProductAdapter(it, vm)
            }
        }

        b.findDishesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_productsDishesEvalFragment_dishesFragment)
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
