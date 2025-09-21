package com.x0Asian.MxM.data.model

data class Post(
    val userName: String,
    val content: String,
    val profileIconResId: Int, // Using Int for drawable resource ID
    var isLiked: Boolean // 'var' if you plan to change it, 'val' if it's immutable after creation
)