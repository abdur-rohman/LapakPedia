package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.rohman.lapakpedia.R
import dev.rohman.lapakpedia.databinding.FragmentLoginBinding
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import dev.rohman.lapakpedia.repos.remote.requests.LoginRequest
import dev.rohman.lapakpedia.utils.isAlphanumeric
import dev.rohman.lapakpedia.utils.isEmail
import dev.rohman.lapakpedia.utils.showError
import dev.rohman.lapakpedia.viewmodels.LoginState
import dev.rohman.lapakpedia.viewmodels.LoginViewModel
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by inject<LoginViewModel>()
    private val localStorage by inject<LocalStorage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            btnLogin.setOnClickListener {
                if (tieEmail.text.isEmail && tiePassword.text.isAlphanumeric) {
                    viewModel.login(
                        request = LoginRequest(
                            email = tieEmail.text.toString(),
                            password = tiePassword.text.toString()
                        )
                    )
                } else {
                    root.showError("Email tidak valid atau password bukan kombinasi huruf dan angka")
                }
            }

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is LoginState.Loading -> showLoading(true)
                    is LoginState.Error -> {
                        showLoading(false)

                        root.showError(it.exception.message)
                    }
                    is LoginState.SuccessLogin -> {
                        showLoading(false)

                        localStorage.clear()
                        localStorage.user = it.data

                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                    else -> throw IllegalStateException("Unsupported state type")
                }
            }
        }

        if (localStorage.user.token.isNotEmpty()) findNavController().navigate(R.id.action_loginFragment_to_mainFragment)

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnLogin.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.pbLoading.visibility = if (!isLoading) View.GONE else View.VISIBLE
    }
}