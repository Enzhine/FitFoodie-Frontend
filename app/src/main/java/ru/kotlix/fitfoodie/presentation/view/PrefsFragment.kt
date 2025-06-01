package ru.kotlix.fitfoodie.presentation.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentPrefsBinding
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
                    lifecycleScope.launch {
                        vm.resetCredentials()
                        navigateStart()
                    }
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
    }

    private fun navigateStart() {
        val activity = requireActivity()

        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity.finish()
    }
}
