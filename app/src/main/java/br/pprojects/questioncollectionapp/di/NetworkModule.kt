package br.pprojects.questioncollectionapp.di

import br.pprojects.questioncollectionapp.data.network.ApiService
import br.pprojects.questioncollectionapp.data.network.RetrofitManager
import org.koin.dsl.module

val networkModule = module {
    single { RetrofitManager() }
    single { ApiService.create() }
}