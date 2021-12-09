package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.repos.auth.AuthRepository

sealed class AuthState {
    data class Error(val exception: Exception) : AuthState()
    data class SuccessLogout(val data: UserModel) : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : BaseViewModel() {
    private val mutableState by lazy { MutableLiveData<AuthState>() }
    val state: LiveData<AuthState> get() = mutableState

    fun logout() {
        coroutines { mutableState.postValue(AuthState.SuccessLogout(repository.logout())) }
    }

    private fun coroutines(run: suspend () -> Unit) {
        callCoroutines(run) { mutableState.postValue(AuthState.Error(it)) }
    }
}