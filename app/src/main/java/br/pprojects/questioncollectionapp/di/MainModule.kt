package br.pprojects.questioncollectionapp.di

import br.pprojects.questioncollectionapp.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MainViewModel(get()) }
}