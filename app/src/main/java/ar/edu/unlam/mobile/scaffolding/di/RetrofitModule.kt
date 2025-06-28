package ar.edu.unlam.mobile.scaffolding.di

import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.AuthApiService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.PostApiService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.ProfileApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val BASE_URL = "https://tuiter.fragua.com.ar"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService = retrofit.create(ProfileApiService::class.java)

    @Provides
    @Singleton
    fun providePostApiService(retrofit: Retrofit): PostApiService = retrofit.create(PostApiService::class.java)
}
