package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import dev.rohman.lapakpedia.databinding.FragmentRegisterBinding
import dev.rohman.lapakpedia.utils.getSpinnerAdapter
import dev.rohman.lapakpedia.utils.showError
import dev.rohman.lapakpedia.viewmodels.RegisterState
import dev.rohman.lapakpedia.viewmodels.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModel<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false).apply {
            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is RegisterState.Loading -> showLoading(true)
                    is RegisterState.Error -> {
                        showLoading(false)

                        root.showError(it.exception.message)
                    }
                    is RegisterState.SuccessGetAllProvince -> {
                        showLoading(false)

                        val provinces = it.data.asSequence()
                            .map { model -> model.province }.toList()

                        spProvince.adapter = requireContext().getSpinnerAdapter(provinces)
                    }
                    is RegisterState.SuccessFilterCities -> {
                        val cities = it.data.asSequence()
                            .map { model -> model.cityName }.toList()

                        spCity.adapter = requireContext().getSpinnerAdapter(cities)
                    }
                    else -> throw IllegalStateException("Unsupported state type")
                }

                spProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.filterCitiesByIdProvince(position)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }

        viewModel.getAllRegion()

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnRegister.visibility = if (!isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        viewModel.cancelAllJob()

        super.onDestroy()
    }
}