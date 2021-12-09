package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.repos.remote.requests.TokenRequest
import dev.rohman.lapakpedia.repos.user.UserRepository

sealed class MainState {
    data class SuccessUpdateToken(val data: UserModel) : MainState()
    data class Error(val exception: Exception) : MainState()
}

class MainViewModel(private val repository: UserRepository) : BaseViewModel() {
    private val mutableState by lazy { MutableLiveData<MainState>() }
    val state: LiveData<MainState> get() = mutableState

    fun updateToken(request: TokenRequest) {
        callCoroutines({
            mutableState.postValue(MainState.SuccessUpdateToken(repository.updateNotificationToken(request)))
        }) { mutableState.postValue(MainState.Error(it)) }
    }
}