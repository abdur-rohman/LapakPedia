package dev.rohman.lapakpedia.utils.modules

import android.content.Context
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import dev.rohman.lapakpedia.utils.ConstantUtil
import org.koin.dsl.module

val storageModule = module {
    factory { get<Context>().getSharedPreferences(ConstantUtil.SHARED_NAME, Context.MODE_PRIVATE) }
    factory { LocalStorage(get()) }
}