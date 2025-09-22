package com.x0Asian.MxM.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "posts") // Added tableName for clarity
data class Post(
    @PrimaryKey
    var postId: String = "", // Unique ID for the post

    var authorId: String = "", // ID of the user who created the post
    var authorName: String = "", // Name of the post author (denormalized for easy display)
    var authorProfileImageUrl: String = "", // URL of author's profile image (denormalized)

    var textContent: String = "",
    var postImageUrl: String = "", // Optional URL for an image attached to the post
    var createdAt: Date? = null,
    var likesCount: Int = 0
)
