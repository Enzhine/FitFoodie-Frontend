package ru.kotlix.fitfoodie.presentation.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentPrefsBinding
import ru.kotlix.fitfoodie.domain.dto.UserPreferences
import ru.kotlix.fitfoodie.presentation.viewmodel.PrefsFragmentViewModel

@AndroidEntryPoint
class PrefsFragment : Fragment(R.layout.fragment_prefs) {
    private var _b: FragmentPrefsBinding? = null
    private val b get() = _b!!

    private val vm: PrefsFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentPrefsBinding.bind(view)

        b.profileToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_exit -> {
                    showExitDialog()
                    true
                }

                else -> false
            }
        }

        lifecycleScope.launch {
            vm.userName.collect {
                if (it != null) {
                    b.profileToolbar.title = getString(R.string.profileName, it)
                }
            }
        }
        b.prefsCategoryHelp.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.prefsCategoryHelp))
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }

        lifecycleScope.launch {
            vm.meatPreference.collect {
                val idToSelect = when (it) {
                    UserPreferences.ProductKind.LIKE -> b.prefsCategoryMeatRgLike.id
                    UserPreferences.ProductKind.RARE -> b.prefsCategoryMeatRgRare.id
                    UserPreferences.ProductKind.EXCL -> b.prefsCategoryMeatRgExcl.id
                    null -> return@collect
                }
                b.prefsCategoryMeatRg.check(idToSelect)
            }
        }
        lifecycleScope.launch {
            vm.fishPreference.collect {
                val idToSelect = when (it) {
                    UserPreferences.ProductKind.LIKE -> b.prefsCategoryFishRgLike.id
                    UserPreferences.ProductKind.RARE -> b.prefsCategoryFishRgRare.id
                    UserPreferences.ProductKind.EXCL -> b.prefsCategoryFishRgExcl.id
                    null -> return@collect
                }
                b.prefsCategoryFishRg.check(idToSelect)
            }
        }
        lifecycleScope.launch {
            vm.milkPreference.collect {
                val idToSelect = when (it) {
                    UserPreferences.ProductKind.LIKE -> b.prefsCategoryMilkRgLike.id
                    UserPreferences.ProductKind.RARE -> b.prefsCategoryMilkRgRare.id
                    UserPreferences.ProductKind.EXCL -> b.prefsCategoryMilkRgExcl.id
                    null -> return@collect
                }
                b.prefsCategoryMilkRg.check(idToSelect)
            }
        }

        b.prefsCategoryMeatRg.setOnCheckedChangeListener { group, checkedId ->
            lifecycleScope.launch {
                when (checkedId) {
                    R.id.prefsCategoryMeatRgLike -> vm.setMeatPreference(UserPreferences.ProductKind.LIKE)
                    R.id.prefsCategoryMeatRgRare -> vm.setMeatPreference(UserPreferences.ProductKind.RARE)
                    R.id.prefsCategoryMeatRgExcl -> vm.setMeatPreference(UserPreferences.ProductKind.EXCL)
                }
            }
        }
        b.prefsCategoryFishRg.setOnCheckedChangeListener { group, checkedId ->
            lifecycleScope.launch {
                when (checkedId) {
                    R.id.prefsCategoryFishRgLike -> vm.setFishPreference(UserPreferences.ProductKind.LIKE)
                    R.id.prefsCategoryFishRgRare -> vm.setFishPreference(UserPreferences.ProductKind.RARE)
                    R.id.prefsCategoryFishRgExcl -> vm.setFishPreference(UserPreferences.ProductKind.EXCL)
                }
            }
        }
        b.prefsCategoryMilkRg.setOnCheckedChangeListener { group, checkedId ->
            lifecycleScope.launch {
                when (checkedId) {
                    R.id.prefsCategoryMilkRgLike -> vm.setMilkPreference(UserPreferences.ProductKind.LIKE)
                    R.id.prefsCategoryMilkRgRare -> vm.setMilkPreference(UserPreferences.ProductKind.RARE)
                    R.id.prefsCategoryMilkRgExcl -> vm.setMilkPreference(UserPreferences.ProductKind.EXCL)
                }
            }
        }
    }

    private fun showExitDialog() {
        val ctx = requireContext()

        AlertDialog.Builder(ctx)
            .setTitle(getString(R.string.exit_warning))
            .setPositiveButton(getString(R.string.exit_accept)) { _, _ ->
                lifecycleScope.launch {
                    vm.resetCredentials()
                    navigateStart()
                }
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun navigateStart() {
        val activity = requireActivity()

        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.syncPreferences()
        _b = null
    }
}
