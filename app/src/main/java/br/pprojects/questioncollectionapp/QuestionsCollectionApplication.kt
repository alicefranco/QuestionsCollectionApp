package br.pprojects.questioncollectionapp

import android.app.Application
import br.pprojects.questioncollectionapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class QuestionsCollectionApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@QuestionsCollectionApplication)
            modules(
                listOf(
                    splashModule,
                    mainModule,
                    networkModule,
                    questionsCollectionModule,
                    questionDetailsModule
                )
            )
        }
    }
}