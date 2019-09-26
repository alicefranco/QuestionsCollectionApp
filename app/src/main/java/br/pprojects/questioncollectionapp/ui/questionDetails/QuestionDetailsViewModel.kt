package br.pprojects.questioncollectionapp.ui.questionDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.data.model.ResultAPI
import br.pprojects.questioncollectionapp.data.repository.QuestionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionDetailsViewModel(private val repository: QuestionsRepository) : ViewModel() {
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()
    var updated: MutableLiveData<Boolean> = MutableLiveData()

    fun updateQuestion(question: Question) {
        CoroutineScope(Dispatchers.Main).launch {
            loading.value = true
            val response = repository.updateQuestion(question)
            loading.value = false

            when (response) {
                is ResultAPI.Success -> {
                    updated.value = true
                }
                is ResultAPI.Error -> {
                    updated.value = false
                    error.value = response.error.errorDescription
                }
                is ResultAPI.InternalError -> {
                    updated.value = false
                    error.value = "Please, check your internet connection."
                }
            }
        }
    }
}
