package dev.rohman.lapakpedia.utils.modules

import dev.rohman.lapakpedia.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { CartsViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { ProductsViewModel(get(), get()) }
    viewModel { ProductViewModel(get(), get(), get()) }
}