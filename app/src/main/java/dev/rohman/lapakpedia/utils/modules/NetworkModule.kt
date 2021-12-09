package dev.rohman.lapakpedia.utils.modules

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.rohman.lapakpedia.BuildConfig
import dev.rohman.lapakpedia.repos.remote.services.*
import dev.rohman.lapakpedia.utils.ConstantUtil
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<Gson> { GsonBuilder().setLenient().create() }

    single {
        HttpLoggingInterceptor { Log.e("API_LOG", it) }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    factory(named(ConstantUtil.LAPAK_PEDIA_CLIENT)) {
        val localStorage by lazy { get<LocalStorage>() }

        OkHttpClient.Builder()
            .apply {
                callTimeout(1, TimeUnit.MINUTES)
                connectTimeout(1, TimeUnit.MINUTES)
                readTimeout(1, TimeUnit.MINUTES)
                writeTimeout(1, TimeUnit.MINUTES)
                retryOnConnectionFailure(true)

                if (BuildConfig.DEBUG) addInterceptor(get<HttpLoggingInterceptor>())

                addInterceptor(Interceptor {
                    val origin = it.request()

                    val request = origin.newBuilder()
                        .addHeader("Authorization", "Bearer ${localStorage.user.token}")
                        .build()

                    return@Interceptor it.proceed(request)
                })
            }
            .build()
    }

    single(named(ConstantUtil.RAJA_ONGKIR_CLIENT)) {
        OkHttpClient.Builder()
            .apply {
                callTimeout(1, TimeUnit.MINUTES)
                connectTimeout(1, TimeUnit.MINUTES)
                readTimeout(1, TimeUnit.MINUTES)
                writeTimeout(1, TimeUnit.MINUTES)
                retryOnConnectionFailure(true)

                if (BuildConfig.DEBUG) addInterceptor(get<HttpLoggingInterceptor>())

                addInterceptor(Interceptor {
                    val origin = it.request()

                    val request = origin.newBuilder()
                        .addHeader("key", ConstantUtil.RAJA_ONGKIR_API_KEY)
                        .build()

                    return@Interceptor it.proceed(request)
                })
            }
            .build()
    }

    single(named(ConstantUtil.RAJA_ONGKIR_RETROFIT)) {
        getRetrofit(
            ConstantUtil.RAJA_ONGKIR_BASE_URL,
            get(),
            get(named(ConstantUtil.RAJA_ONGKIR_CLIENT))
        )
    }

    single(named(ConstantUtil.LAPAK_PEDIA_RETROFIT)) {
        getRetrofit(
            ConstantUtil.LAPAK_PEDIA_BASE_URL,
            get(),
            get(named(ConstantUtil.LAPAK_PEDIA_CLIENT))
        )
    }

    single<RajaOngkirService> {
        get<Retrofit>(named(ConstantUtil.RAJA_ONGKIR_RETROFIT)).create(RajaOngkirService::class.java)
    }

    single<AuthService> {
        get<Retrofit>(named(ConstantUtil.LAPAK_PEDIA_RETROFIT)).create(AuthService::class.java)
    }

    single<OrderService> {
        get<Retrofit>(named(ConstantUtil.LAPAK_PEDIA_RETROFIT)).create(OrderService::class.java)
    }

    single<UserService> {
        get<Retrofit>(named(ConstantUtil.LAPAK_PEDIA_RETROFIT)).create(UserService::class.java)
    }

    single<ProductService> {
        get<Retrofit>(named(ConstantUtil.LAPAK_PEDIA_RETROFIT)).create(ProductService::class.java)
    }

    single<FavoriteService> {
        get<Retrofit>(named(ConstantUtil.LAPAK_PEDIA_RETROFIT)).create(FavoriteService::class.java)
    }
}

fun getRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()