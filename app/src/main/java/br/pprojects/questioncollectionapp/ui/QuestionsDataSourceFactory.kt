package br.pprojects.questioncollectionapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.data.repository.QuestionsRepository

class QuestionsDataSourceFactory(val repository: QuestionsRepository) : DataSource.Factory<Int, Question>() {
    var questionsLiveDataSource: MutableLiveData<QuestionDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, Question> {
        val questionDataSource = QuestionDataSource(repository)
        questionsLiveDataSource.postValue(questionDataSource)
        return questionDataSource
    }

}
