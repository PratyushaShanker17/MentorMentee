package com.x0Asian.MxM.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.x0Asian.MxM.data.models.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(chatMessage: ChatMessage)

    // Get all messages for a specific conversation, ordered by timestamp
    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getChatMessagesByConversationId(conversationId: String): Flow<List<ChatMessage>>

    // Get the last message for a specific conversation (useful for message previews)
    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp DESC LIMIT 1")
    fun getLastChatMessageByConversationId(conversationId: String): Flow<ChatMessage?>

    // Optional: Delete all messages in a conversation
    @Query("DELETE FROM chat_messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesByConversationId(conversationId: String)

    // Optional: Delete a specific message by its ID
    @Query("DELETE FROM chat_messages WHERE id = :messageId")
    suspend fun deleteMessageById(messageId: String)
}
