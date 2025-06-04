package ru.kotlix.fitfoodie.presentation.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentDishBinding
import ru.kotlix.fitfoodie.presentation.viewmodel.DishFragmentViewModel
import kotlin.getValue

@AndroidEntryPoint
class DishFragment : Fragment(R.layout.fragment_dish) {
    private var _b: FragmentDishBinding? = null
    private val b get() = _b!!

    private val vm: DishFragmentViewModel by viewModels()
    private val args: DishFragmentArgs by navArgs()

    private var propsExpanded = false
    private var recipeExpanded = false
    private var chefExpanded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentDishBinding.bind(view)

        b.blockingOverlay.visibility = View.VISIBLE

        lifecycleScope.launch {
            val dish = vm.getDish(args.id)
            if (dish == null) {
                Snackbar.make(b.root, "Не удалось получить данные.", Snackbar.LENGTH_SHORT)
                    .show()

                requireActivity().onBackPressedDispatcher.onBackPressed()
                return@launch
            }

            b.dishTitle.title = dish.title
            Glide.with(view)
                .load(requireContext().getString(R.string.baseUrlDishImage, dish.id.toString()))
                .into(b.dishImage)

            // content
            b.dishProps.cardDishTitle.text = getString(R.string.dishProps)
            b.dishProps.cardDishContent.text =
                dish.props.entries.joinToString(System.lineSeparator()) { "${it.key}: ${it.value}" }
            // ui
            b.dishProps.cardDishBtn.setImageResource(
                if (propsExpanded) R.drawable.ic_shrink else R.drawable.ic_expand
            )
            b.dishProps.cardDishBtn.setOnClickListener {
                if (propsExpanded) {
                    b.dishProps.cardDishContent.maxLines = 4
                    b.dishProps.cardDishContent.ellipsize = TextUtils.TruncateAt.END
                    propsExpanded = !propsExpanded
                } else {
                    b.dishProps.cardDishContent.maxLines = Int.MAX_VALUE
                    b.dishProps.cardDishContent.ellipsize = null
                    propsExpanded = !propsExpanded
                }
                b.dishProps.cardDishBtn.setImageResource(
                    if (propsExpanded) R.drawable.ic_shrink else R.drawable.ic_expand
                )
            }

            // content
            b.dishRecipe.cardDishTitle.text = getString(R.string.dishRecipe)
            b.dishRecipe.cardDishContent.text = dish.recipe
            //ui
            b.dishRecipe.cardDishBtn.setImageResource(
                if (recipeExpanded) R.drawable.ic_shrink else R.drawable.ic_expand
            )
            b.dishRecipe.cardDishBtn.setOnClickListener {
                if (recipeExpanded) {
                    b.dishRecipe.cardDishContent.maxLines = 4
                    b.dishRecipe.cardDishContent.ellipsize = TextUtils.TruncateAt.END
                    recipeExpanded = !recipeExpanded
                } else {
                    b.dishRecipe.cardDishContent.maxLines = Int.MAX_VALUE
                    b.dishRecipe.cardDishContent.ellipsize = null
                    recipeExpanded = !recipeExpanded
                }
                b.dishRecipe.cardDishBtn.setImageResource(
                    if (recipeExpanded) R.drawable.ic_shrink else R.drawable.ic_expand
                )
            }

            if (dish.chefAdvice != null) {
                // content
                b.dishChef.cardDishTitle.text = getString(R.string.dishChef)
                b.dishChef.cardDishContent.text = dish.chefAdvice
                //ui
                b.dishChef.cardDishBtn.setImageResource(
                    if (chefExpanded) R.drawable.ic_shrink else R.drawable.ic_expand
                )
                b.dishChef.cardDishBtn.setOnClickListener {
                    if (chefExpanded) {
                        b.dishChef.cardDishContent.maxLines = 4
                        b.dishChef.cardDishContent.ellipsize = TextUtils.TruncateAt.END
                        chefExpanded = !chefExpanded
                    } else {
                        b.dishChef.cardDishContent.maxLines = Int.MAX_VALUE
                        b.dishChef.cardDishContent.ellipsize = null
                        chefExpanded = !chefExpanded
                    }
                    b.dishChef.cardDishBtn.setImageResource(
                        if (chefExpanded) R.drawable.ic_shrink else R.drawable.ic_expand
                    )
                }
            } else {
                b.dishChef.cardDish.visibility = View.GONE
            }

            b.blockingOverlay.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _b = null
    }
}
