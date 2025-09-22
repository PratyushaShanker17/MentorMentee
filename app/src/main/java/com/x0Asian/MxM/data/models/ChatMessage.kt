package com.x0Asian.MxM.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val conversationId: String, // To link messages to a specific chat (e.g., with another user)
    val senderId: String,       // ID of the user who sent this message
    val text: String,
    val timestamp: Long,
    val isMine: Boolean         // True if this message was sent by the current logged-in user
    // Consider adding other fields like readStatus, messageType (text, image), etc. later
)
