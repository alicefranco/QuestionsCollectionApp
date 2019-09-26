package br.pprojects.questioncollectionapp.di

import br.pprojects.questioncollectionapp.ui.splashScreen.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}