package br.pprojects.questioncollectionapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pprojects.questioncollectionapp.data.repository.QuestionsRepository

class MainViewModel(private val repository: QuestionsRepository) : ViewModel() {
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()

}