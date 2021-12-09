package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.rohman.lapakpedia.R
import dev.rohman.lapakpedia.databinding.FragmentOnboardBinding
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import org.koin.android.ext.android.inject

class OnboardFragment : Fragment() {

    private lateinit var binding: FragmentOnboardBinding
    private val localStorage by inject<LocalStorage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardBinding.inflate(inflater, container, false).apply {
            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_onboardFragment_to_registerFragment)
            }

            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_onboardFragment_to_loginFragment)
            }
        }

        if (localStorage.user.token.isNotEmpty()) findNavController().navigate(R.id.action_onboardFragment_to_loginFragment)

        return binding.root
    }
}