package br.pprojects.questioncollectionapp.data.repository

import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.data.model.ResultAPI
import br.pprojects.questioncollectionapp.data.model.Status
import br.pprojects.questioncollectionapp.data.network.ApiService
import br.pprojects.questioncollectionapp.utils.result
import br.pprojects.questioncollectionapp.utils.safeCall
import org.koin.core.KoinComponent

interface QuestionsRepository {
    suspend fun checkHealth(): ResultAPI<Status>
    suspend fun listQuestions(limit: Int, offset: Int, filter: String? = null): ResultAPI<List<Question>>
    suspend fun searchQuestion(questionId: Int): ResultAPI<Question>
    suspend fun shareQuestion(emailDestination: String, contentUrl: String): ResultAPI<Status>
}

class QuestionsRepositoryImpl(private val apiService: ApiService) : QuestionsRepository, KoinComponent {
    override suspend fun checkHealth(): ResultAPI<Status> {
        val response = safeCall { apiService.checkHealth() }
        return response.result()
    }

    override suspend fun listQuestions(limit: Int, offset: Int, filter: String?): ResultAPI<List<Question>> {
        val response = safeCall { apiService.listQuestions(limit, offset, filter) }
        return response.result()
    }

    override suspend fun searchQuestion(questionId: Int): ResultAPI<Question> {
        val response = safeCall { apiService.searchQuestion(questionId) }
        return response.result()
    }

    // the endpoint for this method is not working
    override suspend fun shareQuestion(emailDestination: String, contentUrl: String): ResultAPI<Status> {
        val response = safeCall { apiService.shareQuestion(emailDestination, contentUrl) }

        return response.result()
    }
}