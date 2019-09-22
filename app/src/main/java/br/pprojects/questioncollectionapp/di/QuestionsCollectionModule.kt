package br.pprojects.questioncollectionapp.di

import br.pprojects.questioncollectionapp.data.repository.QuestionsRepository
import br.pprojects.questioncollectionapp.data.repository.QuestionsRepositoryImpl
import org.koin.dsl.module

val questionsCollectionModule = module {
    single<QuestionsRepository> { QuestionsRepositoryImpl(get()) }
}