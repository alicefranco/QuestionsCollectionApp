package br.pprojects.questioncollectionapp.di

import br.pprojects.questioncollectionapp.ui.questionDetails.QuestionDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val questionDetailsModule = module {
    viewModel { QuestionDetailsViewModel(get()) }
}