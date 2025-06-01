package ru.kotlix.fitfoodie.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kotlix.fitfoodie.R
import ru.kotlix.fitfoodie.presentation.state.PreauthState
import ru.kotlix.fitfoodie.presentation.viewmodel.MainActivityViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val vm: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootView: View = findViewById(android.R.id.content)

        lifecycleScope.launch {
            when (vm.tryFetchCredentials()) {
                PreauthState.EMPTY -> {
                }

                PreauthState.OK -> {
                    navigateHome()
                }

                PreauthState.ERROR -> {
                    Snackbar.make(rootView, getString(R.string.authRequired), Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun navigateHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}