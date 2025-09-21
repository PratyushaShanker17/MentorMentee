package com.x0Asian.MxM.ui.home // Or com.x0Asian.MxM.adapters if you prefer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mentormentee.R // Assuming this is your R file's package
import com.x0Asian.MxM.data.model.Post // Import your Post data class

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileIcon: ImageView = itemView.findViewById(R.id.imageViewProfileIcon)
        private val profileName: TextView = itemView.findViewById(R.id.textViewProfileName)
        private val postContent: TextView = itemView.findViewById(R.id.textViewPostContent)
        private val likeButton: ImageButton = itemView.findViewById(R.id.buttonLike)

        fun bind(post: Post) {
            profileName.text = post.userName
            postContent.text = post.content
            // You might need to load the profileIcon using an image loading library like Coil or Glide
            // For now, setting a placeholder or the direct resource ID if it's a local drawable
            if (post.profileIconResId != 0) { // Assuming 0 is not a valid resource ID
                 profileIcon.setImageResource(post.profileIconResId)
            } else {
                // Set a default icon if none is provided
                profileIcon.setImageResource(R.drawable.ic_account) // Example placeholder
            }


            // Handle like button state and clicks
            // For simplicity, just toggling a visual state or logging for now
            likeButton.setOnClickListener {
                // Handle like action, e.g., update post.isLiked and notify adapter
                // For now, just a log message
                android.util.Log.d("PostAdapter", "Like button clicked for post by ${post.userName}")
                // You would typically update the like status in your data and UI
            }

            // Update like button icon based on post.isLiked (if you implement that)
            // if (post.isLiked) {
            // likeButton.setImageResource(R.drawable.ic_liked) // replace with your liked icon
            // } else {
            // likeButton.setImageResource(R.drawable.ic_like_border) // replace with your unliked icon
            // }
        }
    }
}