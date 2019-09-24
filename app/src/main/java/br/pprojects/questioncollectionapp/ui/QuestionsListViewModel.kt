package br.pprojects.questioncollectionapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.data.repository.QuestionsRepository

class QuestionsViewModel(repository: QuestionsRepository) : ViewModel() {
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()
    private val pageSize = 10
    var questions: LiveData<PagedList<Question>> = MutableLiveData()
    private var questionsDataSource: LiveData<QuestionDataSource> = MutableLiveData()
    private val questionsDataSourceFactory: QuestionsDataSourceFactory


    init {
        questionsDataSourceFactory = QuestionsDataSourceFactory(repository)
        questionsDataSource = questionsDataSourceFactory.questionsLiveDataSource
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(pageSize*2)
            .setEnablePlaceholders(false)
            .build()

        questions = LivePagedListBuilder(questionsDataSourceFactory, config).build()
    }
}