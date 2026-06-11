package com.example.iptvplayer.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.iptvplayer.BrandConfig
import com.example.iptvplayer.R
import com.example.iptvplayer.databinding.ActivityLoginBinding
import com.example.iptvplayer.di.ServiceLocator
import com.example.iptvplayer.ui.browse.MainActivity
import kotlinx.coroutines.launch

/**
 * Username + password only. The portal/server address is fixed in
 * [BrandConfig] and never shown as an editable field.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val repository get() = ServiceLocator.repository
    private val session get() = ServiceLocator.session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Already signed in? Skip straight to the channel list.
        if (session.isLoggedIn()) {
            goToMain()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvPortal.text = getString(R.string.connected_to, BrandConfig.displayHost())
        binding.btnLogin.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
        val username = binding.etUsername.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString()?.trim().orEmpty()

        if (username.isEmpty() || password.isEmpty()) {
            showError(getString(R.string.error_empty_fields))
            return
        }

        setLoading(true)
        lifecycleScope.launch {
            try {
                val response = repository.authenticate(username, password)
                val info = response.userInfo
                when {
                    info?.auth == 1 && info.status.equals("Active", ignoreCase = true) -> {
                        session.save(username, password)
                        goToMain()
                    }
                    info?.auth == 1 -> showError(
                        getString(R.string.error_account_status, info.status ?: "?")
                    )
                    else -> showError(getString(R.string.error_invalid_credentials))
                }
            } catch (e: Exception) {
                showError(getString(R.string.error_network, e.localizedMessage ?: ""))
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !loading
        binding.etUsername.isEnabled = !loading
        binding.etPassword.isEnabled = !loading
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
