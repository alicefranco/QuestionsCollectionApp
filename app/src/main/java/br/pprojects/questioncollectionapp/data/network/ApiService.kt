package br.pprojects.questioncollectionapp.data.network

import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.data.model.Status
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/health")
    fun checkHealth(): Call<Status>

    @GET("/questions")
    fun listQuestions(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("filter") filter: String?
    ): Call<List<Question>>

    @GET("/questions/{question_id}")
    fun searchQuestion(@Path("question_id") questionId: Int): Call<Question>

    @GET("/share")
    fun shareQuestion(
        @Query("destination_email") destinationEmail: String,
        @Query("content_url") contentUrl: String
    ): Call<Status>

    @PUT("/questions/{question_id}")
    fun updateQuestion(
        @Path("question_id") questionId: Int,
        @Body question: Question
    ): Call<Status>

    companion object {
        fun create(): ApiService {
            return RetrofitManager()
                .build()
                .create(ApiService::class.java)
        }
    }
}