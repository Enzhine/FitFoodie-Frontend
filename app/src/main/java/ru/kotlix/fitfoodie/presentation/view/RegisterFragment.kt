package ru.kotlix.fitfoodie.presentation.view

import android.os.Bundle
import android.view.View
import android.widget.Toast.LENGTH_SHORT
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.databinding.FragmentRegisterBinding
import ru.kotlix.fitfoodie.presentation.state.RegistrationState
import ru.kotlix.fitfoodie.presentation.viewmodel.RegisterFragmentViewModel

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private var _b: FragmentRegisterBinding? = null
    private val b get() = _b!!

    private val vm: RegisterFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentRegisterBinding.bind(view)

        b.etName.doAfterTextChanged { vm.name.value = it.toString() }
        b.etMail.doAfterTextChanged { vm.email.value = it.toString() }
        b.etPassword.doAfterTextChanged { vm.password.value = it.toString() }

        b.btnRegisterSubmit.setOnClickListener { vm.register() }

        viewLifecycleOwner.lifecycleScope.launch { registerErrorCallbacks() }
    }

    private suspend fun registerErrorCallbacks() =
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                vm.nameError.collect { b.tilName.error = it }
            }
            launch {
                vm.emailError.collect { b.tilMail.error = it }
            }
            launch {
                vm.passwordError.collect { b.tilPassword.error = it }
            }
            launch {
                vm.isFormValid.collect { b.btnRegisterSubmit.isEnabled = it }
            }
            launch {
                vm.regState.collect { state ->
                    when (state) {
                        is RegistrationState.Loading ->
                            b.progressBar.visibility = View.VISIBLE

                        is RegistrationState.Success -> {
                            b.progressBar.visibility = View.GONE
                            // навигация дальше
                        }

                        is RegistrationState.Error -> {
                            b.progressBar.visibility = View.GONE
                            Snackbar.make(b.root, state.message ?: "Ошибка", Snackbar.LENGTH_SHORT).show()
                        }

                        else -> Unit
                    }
                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _b = null
    }
}