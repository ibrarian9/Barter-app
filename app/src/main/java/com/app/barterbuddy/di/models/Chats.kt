package com.app.barterbuddy.di.models

import com.google.gson.annotations.SerializedName

data class ChatHistoryResponse(
    @SerializedName("messages")
    val messages: List<ChatMessage>
)

data class ChatMessage(
    @SerializedName("senderId")
    val senderId: String,
    @SerializedName("receiverId")
    val receiverId: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("timestamp")
    val timestamp: String
)

data class AddChat(
    @field:SerializedName("senderId")
    val senderId: String,
    @field:SerializedName("receiverId")
    val receiverId: String,
    @field:SerializedName("message")
    val message: String
)