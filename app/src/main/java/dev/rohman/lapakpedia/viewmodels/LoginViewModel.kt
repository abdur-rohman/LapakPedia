package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.repos.auth.AuthRepository
import dev.rohman.lapakpedia.repos.remote.requests.LoginRequest

sealed class LoginState {
    data class Loading(val message: String = "Loading...") : LoginState()
    data class Error(val exception: Exception) : LoginState()
    data class SuccessLogin(val data: UserModel) : LoginState()
}

class LoginViewModel(private val repository: AuthRepository) : BaseViewModel() {
    private val mutableState by lazy { MutableLiveData<LoginState>() }
    val state: LiveData<LoginState> get() = mutableState

    fun login(request: LoginRequest) {
        mutableState.value = LoginState.Loading()

        callCoroutines({
            mutableState.postValue(LoginState.SuccessLogin(repository.login(request)))
        }) { mutableState.postValue(LoginState.Error(it)) }
    }
}