package br.pprojects.questioncollectionapp.ui

import androidx.paging.PageKeyedDataSource
import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.data.model.ResultAPI
import br.pprojects.questioncollectionapp.data.repository.QuestionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionDataSource(val repository: QuestionsRepository) : PageKeyedDataSource<Int, Question>() {
    val PAGE_SIZE = 10
    val FIRST_ITEM = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Question>
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = repository.listQuestions(PAGE_SIZE, FIRST_ITEM)

            when (response){
                is ResultAPI.Success -> {
                    callback.onResult(response.data, null, FIRST_ITEM + PAGE_SIZE)
                }
                is ResultAPI.Error, is ResultAPI.InternalError -> {
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Question>) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = repository.listQuestions(PAGE_SIZE, params.key)

            when (response){
                is ResultAPI.Success -> {
                    callback.onResult(response.data, params.key + PAGE_SIZE)
                }
                is ResultAPI.Error, is ResultAPI.InternalError -> {
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Question>) {}

}
