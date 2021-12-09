package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val jobs by lazy { mutableListOf<Job>() }

    protected fun clearAllJob() = jobs.clear()

    protected fun callCoroutines(coroutine: suspend () -> Unit, onError: (Exception) -> Unit) {
        jobs.add(
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    coroutine()
                } catch (exc: Exception) {
                    exc.printStackTrace()
                    onError(exc)
                }
            }
        )
    }

    fun cancelAllJob() = jobs.asSequence().forEach {
        if (!it.isCompleted) it.cancel()
    }
}