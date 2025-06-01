package ru.kotlix.fitfoodie.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentHomeBinding
import ru.kotlix.fitfoodie.presentation.viewmodel.HomeFragmentViewModel

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!

    private val vm: HomeFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentHomeBinding.bind(view)

        b.btnFindRecipe.setOnClickListener {
            // TODO()
            Snackbar.make(b.root, "b.btnFindRecipe.setOnClickListener", Snackbar.LENGTH_SHORT).show()
        }

        b.btnCalcProducts.setOnClickListener {
            // TODO()
            Snackbar.make(b.root, "b.btnCalcProducts.setOnClickListener", Snackbar.LENGTH_SHORT).show()
        }

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                lifecycleScope.launch {
                    delay(1500)
                    isEnabled = true
                }
                Snackbar.make(b.root, getString(R.string.retryLeave), Snackbar.LENGTH_SHORT).show()
            }
        }
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backCallback)

        lifecycleScope.launch {
            vm.userName.collect {
                if (it != null) {
                    b.welcomeToolbar.title = getString(R.string.welcomeUser, it)
                }
            }
        }
    }
}