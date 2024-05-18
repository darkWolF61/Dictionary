package com.sairam.dictionaryapp.di

import com.sairam.dictionaryapp.data.api.DictionaryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    // just to get logs
    private val interceptor : HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    // creating the client

    private val client : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor).build()


    // creating an instance of Dictionary api
    @Provides
    @Singleton
    fun providesDictionaryApi(): DictionaryApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(DictionaryApi.BASE_URL)
            .client(client) // client needs to be added here only
            .build()
            .create()
    }
}