package com.x0Asian.MxM.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.x0Asian.MxM.R // R file from your app's namespace
import com.x0Asian.MxM.data.model.Post // Your Post data class
// PostAdapter is in the same package, so no explicit import is needed if it's 'com.x0Asian.MxM.ui.home.PostAdapter'

class HomeFragment : Fragment() {

    private lateinit var recyclerViewFeed: RecyclerView
    private lateinit var postAdapter: PostAdapter // Your adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewFeed = view.findViewById(R.id.recyclerViewFeed)
        recyclerViewFeed.layoutManager = LinearLayoutManager(context)

        // Create sample data (replace with your actual data source later)
        val samplePosts = listOf(
            Post("Alice Wonderland", "Just had a wonderful tea party!", R.drawable.ic_account, false),
            Post("Bob The Builder", "Can we fix it? Yes, we can! Check out my latest project.", R.drawable.ic_account, true),
            Post("Charlie Chaplin", "A day without laughter is a day wasted.", R.drawable.ic_account, false),
            Post("Diana Prince", "Working on saving the world, brb.", R.drawable.ic_account, false),
            Post("Edward Scissorhands", "Gardening is quite therapeutic.", R.drawable.ic_account, true)
        )

        postAdapter = PostAdapter(samplePosts)
        recyclerViewFeed.adapter = postAdapter
    }
}