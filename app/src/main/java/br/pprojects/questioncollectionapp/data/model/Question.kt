package br.pprojects.questioncollectionapp.data.model

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("id") var id: Int,
    @SerializedName("question") var question: String,
    @SerializedName("image_url") var imageUrl: String,
    @SerializedName("thumb_url") var thumbUrl: String,
    @SerializedName("published_at") var publishedAt: String,
    @SerializedName("choices") var choices: List<Choice>
)
