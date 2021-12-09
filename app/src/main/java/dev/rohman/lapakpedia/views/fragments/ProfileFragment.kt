package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import dev.rohman.lapakpedia.databinding.FragmentProfileBinding
import dev.rohman.lapakpedia.repos.remote.requests.UserRequest
import dev.rohman.lapakpedia.utils.getSpinnerAdapter
import dev.rohman.lapakpedia.utils.isEmail
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import dev.rohman.lapakpedia.utils.showError
import dev.rohman.lapakpedia.viewmodels.ProfileState
import dev.rohman.lapakpedia.viewmodels.ProfileViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val localStorage by inject<LocalStorage>()
    private val viewModel by viewModel<ProfileViewModel>()
    private var provinceId = 0
    private var cityId = 0
    private var cityName = ""
    private var provinceName = ""
    private var postalCode = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            tieEmail.setText(localStorage.email)
            tieFirstName.setText(localStorage.firstName)
            tieLastName.setText(localStorage.lastName)
            tiePhone.setText(localStorage.phone)

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is ProfileState.Loading -> showLoading(true)
                    is ProfileState.Error -> {
                        showLoading(false)

                        root.showError(it.exception.message)
                    }
                    is ProfileState.SuccessUpdateProfile -> {
                        showLoading(false)

                        localStorage.user = it.user

                        tieEmail.setText(localStorage.email)
                        tieFirstName.setText(localStorage.firstName)
                        tieLastName.setText(localStorage.lastName)
                        tiePhone.setText(localStorage.phone)

                        val provinceIndex = viewModel.provinces.indexOfFirst { province ->
                            province.provinceId.toInt() == localStorage.provinceId
                        }

                        spProvince.setSelection(if (provinceIndex == -1) 0 else provinceIndex)
                    }

                    is ProfileState.SuccessGetAllProvince -> {
                        showLoading(false)

                        val provinces = it.data.asSequence()
                            .map { model -> "Provinsi ${model.province}" }.toList()

                        val index = it.data.indexOfFirst { province ->
                            province.provinceId.toInt() == localStorage.provinceId
                        }

                        spProvince.adapter = requireContext().getSpinnerAdapter(provinces)
                        spProvince.setSelection(if (index == -1) 0 else index)
                    }
                    is ProfileState.SuccessFilterCities -> {
                        showLoading(false)

                        val cities = it.data.asSequence()
                            .map { model -> "${model.type} ${model.cityName}" }.toList()

                        val index = it.data.indexOfFirst { city ->
                            city.cityId.toInt() == localStorage.cityId
                        }

                        spCity.adapter = requireContext().getSpinnerAdapter(cities)
                        spCity.setSelection(if (index == -1) 0 else index)
                    }
                    else -> throw IllegalStateException("Unsupported state type")
                }
            }

            spProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.filterCitiesByIdProvince(position)

                    provinceId = viewModel.provinces[position].provinceId.toInt()
                    provinceName = viewModel.provinces[position].province
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val city = viewModel.filteredCities[position]

                    cityId = city.cityId.toInt()
                    cityName = "${city.type} ${city.cityName}"
                    postalCode = city.postalCode
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            btnUpdate.setOnClickListener {
                if (tieEmail.text.isEmail) {
                    viewModel.updateProfile(
                        UserRequest(
                            firstName = tieFirstName.text.toString(),
                            lastName = tieLastName.text.toString(),
                            email = tieEmail.text.toString(),
                            phone = tiePhone.text.toString(),
                            provinceId = provinceId,
                            provinceName = provinceName,
                            cityId = cityId,
                            cityName = cityName,
                            postalCode = postalCode
                        ), ""
                    )
                } else root.showError("Email tidak valid")
            }
        }

        viewModel.getAllRegion()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnUpdate.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.spProvince.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.spCity.visibility = if (!isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        viewModel.cancelAllJob()

        super.onDestroy()
    }
}