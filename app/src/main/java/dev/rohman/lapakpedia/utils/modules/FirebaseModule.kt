package dev.rohman.lapakpedia.utils.modules

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseMessaging.getInstance() }
    single { Firebase.analytics }
}