package com.x0Asian.MxM.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.x0Asian.MxM.data.local.AppDatabase
import com.x0Asian.MxM.data.local.dao.ChatMessageDao
import com.x0Asian.MxM.data.models.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(application: Application, private val conversationId: String) : AndroidViewModel(application) {

    private val chatMessageDao: ChatMessageDao = AppDatabase.getDatabase(application).chatMessageDao()

    val messages: StateFlow<List<ChatMessage>> = chatMessageDao.getChatMessagesByConversationId(conversationId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun sendMessage(text: String, senderId: String, isMine: Boolean) {
        if (text.isBlank()) return

        viewModelScope.launch {
            val newMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = senderId,
                text = text,
                timestamp = System.currentTimeMillis(),
                isMine = isMine // This should align with senderId == currentUserId
            )
            chatMessageDao.insertChatMessage(newMessage)
        }
    }

    /**
     * Factory for creating ChatViewModel with a constructor that takes conversationId.
     */
    class Factory(private val application: Application, private val conversationId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(application, conversationId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
