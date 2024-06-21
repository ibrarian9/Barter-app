package com.app.barterbuddy.ui.detailChat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.barterbuddy.di.BarterRepository
import com.app.barterbuddy.di.models.AddChat
import com.app.barterbuddy.di.models.ChatHistoryResponse
import com.app.barterbuddy.di.models.ChatMessage
import com.app.barterbuddy.di.models.SuccessPost
import kotlinx.coroutines.launch

class DetailChatViewModel(private val repo: BarterRepository): ViewModel() {

    suspend fun getListChat(userId1: String, userId2: String): LiveData<ChatHistoryResponse> {
        return repo.getListChat(userId1, userId2).asFlow().asLiveData()
    }

    suspend fun postChat(request: AddChat): LiveData<SuccessPost> {
        return repo.postChat(request).asFlow().asLiveData()
    }
}