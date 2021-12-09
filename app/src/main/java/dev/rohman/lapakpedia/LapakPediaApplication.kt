package dev.rohman.lapakpedia

import android.app.Application
import dev.rohman.lapakpedia.utils.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class LapakPediaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.ERROR)
            androidContext(androidContext = this@LapakPediaApplication)
            modules(
                firebaseModule,
                storageModule,
                networkModule,
                daoModule,
                repositoryModule,
                viewModelModule,
            )
        }
    }
}