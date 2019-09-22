package br.pprojects.questioncollectionapp.data.model

import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("choice") var choice: String,
    @SerializedName("votes") var votes: Int
)
