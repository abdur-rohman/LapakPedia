package dev.rohman.lapakpedia.utils.modules

import dev.rohman.lapakpedia.repos.local.databases.LocalDatabase
import org.koin.dsl.module

val daoModule = module {
    factory { LocalDatabase.instance(get()) }
    factory { get<LocalDatabase>().cartDao() }
}