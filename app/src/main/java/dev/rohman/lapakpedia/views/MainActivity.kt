package dev.rohman.lapakpedia.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import dev.rohman.lapakpedia.BuildConfig
import dev.rohman.lapakpedia.databinding.ActivityMainBinding
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import dev.rohman.lapakpedia.utils.showError
import dev.rohman.lapakpedia.viewmodels.AuthState
import dev.rohman.lapakpedia.viewmodels.AuthViewModel
import dev.rohman.lapakpedia.views.fragments.MainFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val localStorage by inject<LocalStorage>()
    private val authViewModel by inject<AuthViewModel>()
    private val firebaseAnalytics by inject<FirebaseAnalytics>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.APP_OPEN, bundleOf(
                FirebaseAnalytics.Param.ORIGIN to BuildConfig.APPLICATION_ID
            )
        )

        authViewModel.state.observe(this) {
            when (it) {
                is AuthState.Error -> binding.root.showError(it.exception.message)
                is AuthState.SuccessLogout -> {
                    localStorage.clear()

                    supportFragmentManager.fragments.first().childFragmentManager.popBackStack()
                }
                else -> throw IllegalStateException("Unsupported state type")
            }
        }
    }
//
//    override fun onBackPressed() {
//        val fragment = supportFragmentManager.fragments.first().childFragmentManager.fragments.first()
//        if (fragment is MainFragment && localStorage.user.token.isNotEmpty()) {
//            authViewModel.logout()
//        }
//
//        super.onBackPressed()
//    }
}