package com.example.androidassignment.model

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    @SerializedName("choices")
    val choices: MutableList<Choice> = mutableListOf()
)

data class Choice(
    @SerializedName("message")
    val message: Message? = null
)

data class Message(
    @SerializedName("content")
    val content: String = ""
)

data class Content(
    val titles: MutableList<String> = mutableListOf(""),
    val description: String = ""
)
