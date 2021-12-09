package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.rohman.lapakpedia.models.CityModel
import dev.rohman.lapakpedia.models.ProvinceModel
import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.repos.rajaongkir.RajaOngkirRepository
import dev.rohman.lapakpedia.repos.remote.requests.UserRequest
import dev.rohman.lapakpedia.repos.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

sealed class ProfileState {
    data class Loading(val message: String = "Loading...") : ProfileState()
    data class SuccessUpdateProfile(val user: UserModel) : ProfileState()
    data class SuccessGetProfile(val user: UserModel) : ProfileState()
    data class SuccessFilterCities(val data: List<CityModel>) : ProfileState()
    data class SuccessGetAllProvince(val data: List<ProvinceModel>) : ProfileState()
    data class Error(val exception: Exception) : ProfileState()
}

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val rajaOngkirRepository: RajaOngkirRepository
) : BaseViewModel() {
    private val mutableState by lazy { MutableLiveData<ProfileState>() }
    val state: LiveData<ProfileState> get() = mutableState

    private var cities = listOf<CityModel>()
    var filteredCities = listOf<CityModel>()
    var provinces = listOf<ProvinceModel>()

    private fun coroutines(run: suspend () -> Unit) {
        mutableState.value = ProfileState.Loading()

        callCoroutines(run) { mutableState.postValue(ProfileState.Error(it)) }
    }

    fun getAllRegion() {
        coroutines {
            coroutineScope {
                val citiesJob = async(Dispatchers.Default) { rajaOngkirRepository.getAllCity() }
                val provincesJob = async(Dispatchers.Default) { rajaOngkirRepository.getAllProvince() }

                cities = citiesJob.await()

                mutableState.postValue(
                    ProfileState.SuccessGetAllProvince(provincesJob.await().also { provinces = it })
                )
            }
        }
    }

    fun filterCitiesByIdProvince(index: Int) {
        coroutines {
            val provinceId = provinces[index].provinceId

            filteredCities = cities.asSequence().filter { it.provinceId == provinceId }.toList()

            mutableState.postValue(ProfileState.SuccessFilterCities(filteredCities))
        }
    }

    fun getProfile() {
        coroutines {
            mutableState.postValue(ProfileState.SuccessGetProfile(userRepository.getProfile()))
        }
    }

    fun updateProfile(request: UserRequest, image: String) {
        coroutines {
            mutableState.postValue(
                ProfileState.SuccessUpdateProfile(userRepository.updateProfile(request, image))
            )
        }
    }
}