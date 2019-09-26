package br.pprojects.questioncollectionapp.ui.splashScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pprojects.questioncollectionapp.data.model.ResultAPI
import br.pprojects.questioncollectionapp.data.repository.QuestionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: QuestionsRepository) : ViewModel() {
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()
    var healthOk: MutableLiveData<Boolean> = MutableLiveData()

    fun checkHealth() {
        CoroutineScope(Dispatchers.Main).launch {
            loading.value = true
            val response = repository.checkHealth()
            loading.value = false

            when (response) {
                is ResultAPI.Success -> {
                    healthOk.value = true
                }
                is ResultAPI.Error -> {
                    healthOk.value = false
                    error.value = response.error.errorDescription
                }
                is ResultAPI.InternalError -> {
                    healthOk.value = false
                    error.value = "Please, check your internet connection."
                }
            }
        }
    }
}