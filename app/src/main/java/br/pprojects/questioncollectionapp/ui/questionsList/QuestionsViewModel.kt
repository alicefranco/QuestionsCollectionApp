package br.pprojects.questioncollectionapp.ui.questionsList

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
import br.pprojects.questioncollectionapp.data.model.ResultAPI
import br.pprojects.questioncollectionapp.util.QuestionDataSource
import br.pprojects.questioncollectionapp.util.QuestionsDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionsViewModel(private val repository: QuestionsRepository) : ViewModel() {
    var loading: LiveData<NetworkState> = MutableLiveData()
    var loadingMutable: MutableLiveData<NetworkState> = MutableLiveData()
    private val pageSize = 10
    var questions: LiveData<PagedList<Question>> = MutableLiveData()
    var questionFound: MutableLiveData<Question> = MutableLiveData()
    private var questionsDataSource: LiveData<QuestionDataSource> = MutableLiveData()
    private lateinit var questionsDataSourceFactory: QuestionsDataSourceFactory

    init {
        questions = listQuestions()
    }

    fun listQuestions(filter: String? = null): LiveData<PagedList<Question>> {
        questionsDataSourceFactory =
            QuestionsDataSourceFactory(filter, repository)
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
        questions = listQuestions(filter)
    }

    fun searchQuestion(questionId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingMutable.value = NetworkState.LOADING
            val response = repository.searchQuestion(questionId)
            when (response) {
                is ResultAPI.Success -> {
                    questionFound.value = response.data
                    loadingMutable.value = NetworkState.DONE
                }
                is ResultAPI.Error -> {
                    loadingMutable.value = NetworkState.ERROR
                }
                is ResultAPI.InternalError -> {
                    loadingMutable.value = NetworkState.NO_CONNECTION
                }
            }
        }
    }
}