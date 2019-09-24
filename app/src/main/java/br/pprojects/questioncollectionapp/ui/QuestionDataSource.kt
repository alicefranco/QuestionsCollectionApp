package br.pprojects.questioncollectionapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import br.pprojects.questioncollectionapp.data.model.NetworkState
import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.data.model.ResultAPI
import br.pprojects.questioncollectionapp.data.repository.QuestionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionDataSource(val filter: String? = null, val repository: QuestionsRepository) : PageKeyedDataSource<Int, Question>() {
    private val PAGE_SIZE = 10
    private val FIRST_ITEM = 1

    var networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Question>
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            networkState.value = NetworkState.LOADING
            val response = repository.listQuestions(PAGE_SIZE, FIRST_ITEM, filter)

            when (response){
                is ResultAPI.Success -> {
                    callback.onResult(response.data, null, FIRST_ITEM + PAGE_SIZE)
                    networkState.value = NetworkState.DONE
                }
                is ResultAPI.Error, is ResultAPI.InternalError -> {
                    networkState.value = NetworkState.ERROR
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Question>) {
        CoroutineScope(Dispatchers.Main).launch {
            networkState.value = NetworkState.LOADING
            val response = repository.listQuestions(PAGE_SIZE, params.key, filter)

            when (response){
                is ResultAPI.Success -> {
                    callback.onResult(response.data, params.key + PAGE_SIZE)
                    networkState.value = NetworkState.DONE
                }
                is ResultAPI.Error, is ResultAPI.InternalError -> {
                    networkState.value = NetworkState.ERROR
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Question>) {}

}
