package ru.kotlix.fitfoodie.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentLoginBinding
import ru.kotlix.fitfoodie.presentation.viewmodel.LoginFragmentViewModel

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _b: FragmentLoginBinding? = null
    private val b get() = _b!!

    private val vm: LoginFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentLoginBinding.bind(view)

    }

    override fun onDestroy() {
        super.onDestroy()
        _b = null
    }
}