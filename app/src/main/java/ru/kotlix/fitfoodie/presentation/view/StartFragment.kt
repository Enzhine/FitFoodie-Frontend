package ru.kotlix.fitfoodie.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentStartBinding

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {
    private var _b: FragmentStartBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentStartBinding.bind(view)

        b.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_loginFragment)
        }
        b.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_registerFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _b = null
    }
}
