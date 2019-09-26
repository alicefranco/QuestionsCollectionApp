package br.pprojects.questioncollectionapp.di

import br.pprojects.questioncollectionapp.ui.questionsList.QuestionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { QuestionsViewModel(get()) }
}