package com.x0Asian.MxM.ui.post

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x0Asian.MxM.data.models.Post // Changed to data.models.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class PostViewModel : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        Log.d("PostVM", "PostViewModel initialized for local data source.")
        fetchPosts() // Initial fetch
    }

    fun createPost(text: String, imageUri: Uri?, authorId: String, authorName: String, authorProfileImageUrl: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("PostVM", "Attempting to create local post by author: $authorId")

            try {
                var localImageFilePath: String? = null
                if (imageUri != null) {
                    Log.d("PostVM", "Local image URI provided (saving not implemented): $imageUri")
                    localImageFilePath = "placeholder_image_path.jpg" // Simulate a saved path
                }

                val newPost = Post(
                    postId = UUID.randomUUID().toString(),
                    authorId = authorId, 
                    authorName = authorName, 
                    authorProfileImageUrl = authorProfileImageUrl, 
                    textContent = text,
                    postImageUrl = localImageFilePath ?: "",
                    createdAt = Date(),
                    likesCount = 0
                )

                val currentPosts = _posts.value.toMutableList()
                currentPosts.add(0, newPost) // Add to the top
                _posts.value = currentPosts

                Log.d("PostVM", "Local post created with ID: ${newPost.postId}")

            } catch (e: Exception) {
                _error.value = "Failed to create local post: ${e.message}"
                Log.e("PostVM", "Failed to create local post", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("PostVM", "Fetching local posts.")
            try {
                if (_posts.value.isEmpty()) { 
                    _posts.value = listOf(
                        Post(
                            postId = UUID.randomUUID().toString(),
                            authorId = "user_robert_johnson",
                            authorName = "Robert Johnson",
                            authorProfileImageUrl = "", // Replace with a placeholder image URL or local path if available
                            textContent = "Seeking a mentor with expertise in Kotlin Multiplatform Mobile to guide a project. Grateful for any leads or advice! #KMM #AndroidDev #iOSDev #Mentorship",
                            postImageUrl = "",
                            createdAt = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24), // 1 day ago
                            likesCount = 27
                        ),
                        Post(
                            postId = UUID.randomUUID().toString(),
                            authorId = "user_maria_garcia",
                            authorName = "Maria Garcia",
                            authorProfileImageUrl = "", // Replace with a placeholder image URL or local path if available
                            textContent = "Just finished a UI/UX case study on improving onboarding flows for mobile apps. Open to feedback and collaborations! #UIUX #Design #MobileApp",
                            postImageUrl = "", 
                            createdAt = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 48), // 2 days ago
                            likesCount = 41
                        ),
                        Post(
                            postId = UUID.randomUUID().toString(),
                            authorId = "user_james_brown",
                            authorName = "James Brown",
                            authorProfileImageUrl = "",
                            textContent = "Excited to share that I've started a new position as a Junior Software Developer! Looking forward to learning and growing. #NewBeginnings #SoftwareDevelopment",
                            postImageUrl = "",
                            createdAt = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 72), // 3 days ago
                            likesCount = 18
                        ),
                        Post(
                            postId = UUID.randomUUID().toString(),
                            authorId = "user_linda_davis",
                            authorName = "Linda Davis",
                            authorProfileImageUrl = "",
                            textContent = "Looking for a mentee interested in learning about cloud infrastructure and AWS. If you're passionate and eager to learn, feel free to reach out! #Mentorship #AWS #CloudComputing",
                            postImageUrl = "",
                            createdAt = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 96), // 4 days ago
                            likesCount = 55
                        )
                    )
                }
                Log.d("PostVM", "Fetched ${_posts.value.size} local posts (stub).")
            } catch (e: Exception) {
                _error.value = "Failed to fetch local posts: ${e.message}"
                Log.e("PostVM", "Failed to fetch local posts", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
