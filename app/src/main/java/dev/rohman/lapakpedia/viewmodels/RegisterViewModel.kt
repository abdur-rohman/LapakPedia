package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.rohman.lapakpedia.models.CityModel
import dev.rohman.lapakpedia.models.ProvinceModel
import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.repos.auth.AuthRepository
import dev.rohman.lapakpedia.repos.rajaongkir.RajaOngkirRepository
import dev.rohman.lapakpedia.repos.remote.requests.UserRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

sealed class RegisterState {
    data class Loading(val message: String = "Loading...") : RegisterState()
    data class Error(val exception: Exception) : RegisterState()
    data class SuccessFilterCities(val data: List<CityModel>) : RegisterState()
    data class SuccessGetAllProvince(val data: List<ProvinceModel>) : RegisterState()
    data class SuccessRegister(val data: UserModel) : RegisterState()
}

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val rajaOngkirRepository: RajaOngkirRepository
) : BaseViewModel() {

    private val mutableState by lazy { MutableLiveData<RegisterState>() }
    val state: LiveData<RegisterState> get() = mutableState

    private var cities = listOf<CityModel>()
    var filteredCities = listOf<CityModel>()
    var provinces = listOf<ProvinceModel>()

    fun getAllRegion() {
        coroutines {
            coroutineScope {
                val citiesJob = async { rajaOngkirRepository.getAllCity() }
                val provincesJob = async { rajaOngkirRepository.getAllProvince() }

                cities = citiesJob.await()

                mutableState.postValue(
                    RegisterState.SuccessGetAllProvince(
                        provincesJob.await().also { provinces = it }
                    )
                )
            }
        }
    }

    fun filterCitiesByIdProvince(index: Int) {
        coroutines {
            val provinceId = provinces[index].provinceId

            filteredCities = cities.asSequence().filter { it.provinceId == provinceId }.toList()

            mutableState.postValue(RegisterState.SuccessFilterCities(filteredCities))
        }
    }

    fun register(request: UserRequest) {
        coroutines {
            mutableState.postValue(
                RegisterState.SuccessRegister(authRepository.register(request))
            )
        }
    }

    private fun coroutines(run: suspend () -> Unit) {
        mutableState.value = RegisterState.Loading()

        callCoroutines(run) { mutableState.postValue(RegisterState.Error(it)) }
    }
}