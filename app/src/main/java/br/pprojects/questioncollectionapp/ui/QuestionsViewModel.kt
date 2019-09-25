package br.pprojects.questioncollectionapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.data.repository.QuestionsRepository
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Transformations
import br.pprojects.questioncollectionapp.data.model.NetworkState

class QuestionsViewModel(private val repository: QuestionsRepository) : ViewModel() {
    var loading: LiveData<NetworkState> = MutableLiveData()
    private val pageSize = 10
    var questions: LiveData<PagedList<Question>> = MutableLiveData()
    private var questionsDataSource: LiveData<QuestionDataSource> = MutableLiveData()
    private lateinit var questionsDataSourceFactory: QuestionsDataSourceFactory

    init {
        questions = searchQuestions()
    }

    fun searchQuestions(filter: String? = null): LiveData<PagedList<Question>> {
        questionsDataSourceFactory = QuestionsDataSourceFactory(filter, repository)
        questionsDataSource = questionsDataSourceFactory.questionsLiveDataSource
        loading = Transformations.switchMap(
                    questionsDataSourceFactory.questionsLiveDataSource
                ) { questionsDataSource -> questionsDataSource.networkState }

        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(pageSize*2)
            .setEnablePlaceholders(false)
            .build()

        return LivePagedListBuilder(questionsDataSourceFactory, config).build()
    }

    fun replaceSubscription(lifecycleOwner: LifecycleOwner, filter: String? = null) {
        questions.removeObservers(lifecycleOwner)
        questions = searchQuestions(filter)
    }
}