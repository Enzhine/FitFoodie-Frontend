package ru.kotlix.fitfoodie.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentLoginBinding
import ru.kotlix.fitfoodie.presentation.state.LoginState
import ru.kotlix.fitfoodie.presentation.viewmodel.LoginFragmentViewModel

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _b: FragmentLoginBinding? = null
    private val b get() = _b!!
    private lateinit var backCallback: OnBackPressedCallback

    private val vm: LoginFragmentViewModel by viewModels()
    private val args: LoginFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentLoginBinding.bind(view)
        fillData()

        backCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {}
        }
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backCallback)

        b.etMail.doAfterTextChanged { vm.email.value = it.toString() }
        b.etPassword.doAfterTextChanged { vm.password.value = it.toString() }

        b.btnLoginSubmit.setOnClickListener { vm.login() }

        viewLifecycleOwner.lifecycleScope.launch { registerErrorCallbacks() }
    }

    private fun fillData() {
        val email = args.email
        val password = args.password

        if (email.isNotEmpty()) {
            b.etMail.setText(email)
            vm.email.value = email

            b.etPassword.isFocusableInTouchMode = true
            b.etPassword.requestFocus()
        }
        if (password.isNotEmpty()) {
            b.etPassword.setText(email)
            vm.password.value = email
        }
    }

    private suspend fun registerErrorCallbacks() =
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                vm.emailError.collect {
                    b.tilMail.error = it
                    b.tilMail.isErrorEnabled = it != null
                }
            }
            launch {
                vm.passwordError.collect {
                    b.tilPassword.error = it
                    b.tilPassword.isErrorEnabled = it != null
                }
            }
            launch {
                vm.isFormValid.collect { b.btnLoginSubmit.isEnabled = it }
            }
            launch {
                vm.loginState.collect { state ->
                    when (state) {
                        is LoginState.Loading -> {
                            b.blockingOverlay.visibility = View.VISIBLE
                            backCallback.isEnabled = true
                        }

                        is LoginState.Success -> {
                            b.blockingOverlay.visibility = View.GONE
                            backCallback.isEnabled = false

                            navigateHome()
                        }

                        is LoginState.Error -> {
                            b.blockingOverlay.visibility = View.GONE
                            backCallback.isEnabled = false

                            Snackbar.make(b.root, state.message ?: "Ошибка", Snackbar.LENGTH_SHORT)
                                .show()
                        }

                        else -> Unit
                    }
                }
            }
        }

    private fun navigateHome() {
        val activity = requireActivity()

        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
        activity.finish()
    }
}