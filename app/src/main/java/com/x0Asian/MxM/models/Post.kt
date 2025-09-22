package com.x0Asian.MxM.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "legacy_posts")
data class Post(
    @PrimaryKey
    var id: String = "",
    var authorUid: String = "",
    var text: String = "",
    var imageUrl: String? = null,
    var timestamp: Date? = null,
    // You might also want:
    // var authorDisplayName: String? = null, // Denormalized for easier display
    // var authorPhotoUrl: String? = null, // Denormalized for easier display
    // var likeCount: Int = 0
)
