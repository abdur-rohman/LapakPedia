package dev.rohman.lapakpedia.utils.modules

import dev.rohman.lapakpedia.repos.auth.AuthRepo
import dev.rohman.lapakpedia.repos.auth.AuthRepository
import dev.rohman.lapakpedia.repos.cart.CartRepo
import dev.rohman.lapakpedia.repos.cart.CartRepository
import dev.rohman.lapakpedia.repos.favorite.FavoriteRepo
import dev.rohman.lapakpedia.repos.favorite.FavoriteRepository
import dev.rohman.lapakpedia.repos.order.OrderRepo
import dev.rohman.lapakpedia.repos.order.OrderRepository
import dev.rohman.lapakpedia.repos.product.ProductRepo
import dev.rohman.lapakpedia.repos.product.ProductRepository
import dev.rohman.lapakpedia.repos.rajaongkir.RajaOngkirRepo
import dev.rohman.lapakpedia.repos.rajaongkir.RajaOngkirRepository
import dev.rohman.lapakpedia.repos.user.UserRepo
import dev.rohman.lapakpedia.repos.user.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepo(get()) }
    single<UserRepository> { UserRepo(get()) }
    single<CartRepository> { CartRepo(get()) }
    single<OrderRepository> { OrderRepo(get()) }
    single<ProductRepository> { ProductRepo(get()) }
    single<FavoriteRepository> { FavoriteRepo(get()) }
    single<RajaOngkirRepository> { RajaOngkirRepo(get()) }
}