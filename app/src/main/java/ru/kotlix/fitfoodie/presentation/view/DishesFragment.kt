package ru.kotlix.fitfoodie.presentation.view

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentDishesBinding
import ru.kotlix.fitfoodie.presentation.adapter.DishAdapter
import ru.kotlix.fitfoodie.presentation.viewmodel.DishesFragmentViewModel

@AndroidEntryPoint
class DishesFragment : Fragment(R.layout.fragment_dishes) {
    private var _b: FragmentDishesBinding? = null
    private val b get() = _b!!

    private val vm: DishesFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentDishesBinding.bind(view)

        b.blockingOverlay.visibility = View.VISIBLE

        b.dishesToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    showSearchDialog()
                    true
                }

                R.id.action_details -> {
                    showDetailsDialog()
                    true
                }

                else -> false
            }
        }

        b.dishesList.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            vm.requestDishes()

            vm.dishes.collect { lst ->
                b.blockingOverlay.visibility = View.GONE

                b.dishesList.adapter = DishAdapter(lst) {
                    navigateToDish(it.id)
                }
            }
        }
    }

    private fun showSearchDialog() {
        val ctx = requireContext()

        val input = EditText(ctx).apply {
            setText(vm.nameFilter.value)
            hint = getString(R.string.search_dishes_hint)
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

    fun showDetailsDialog() {
        val context = requireContext()

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val numberInput1 = EditText(context).apply {
            hint = context.getString(R.string.details_dishes_calories)
            inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(vm.caloriesFilter.value?.toString() ?: "")
        }

        val numberInput2 = EditText(context).apply {
            hint = context.getString(R.string.details_dishes_minutes)
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(vm.cookMinutesFilter.value?.toString() ?: "")
        }

        layout.addView(numberInput1)
        layout.addView(numberInput2)

        AlertDialog.Builder(context)
            .setTitle(getString(R.string.details_dishes_title))
            .setView(layout)
            .setPositiveButton(getString(R.string.btn_search)) { dialog, _ ->
                val cal = numberInput1.text.toString().toBigDecimalOrNull()
                val min = numberInput2.text.toString().toIntOrNull()

                vm.caloriesFilter.value = cal
                vm.cookMinutesFilter.value = min
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun navigateToDish(dishId: Int) {
        val action = DishesFragmentDirections
            .actionDishesFragmentDishFragment(dishId)

        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        findNavController().navigate(
            action,
            options
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _b = null
    }
}
