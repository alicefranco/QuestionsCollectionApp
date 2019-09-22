package br.pprojects.questioncollectionapp.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ClientInterceptor {
    fun createInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = (HttpLoggingInterceptor.Level.BODY)
        }
    }

    fun createClient(): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(createInterceptor())
            .build()
    }
}
