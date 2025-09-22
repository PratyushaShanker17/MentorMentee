package com.x0Asian.MxM.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.x0Asian.MxM.R
import com.x0Asian.MxM.data.model.Post
import com.x0Asian.MxM.ui.navigation.Screen

@Composable
fun rememberSamplePosts(): List<Post> {
    return remember {
        mutableStateListOf(
            Post("Alice Wonderland", "Just had a wonderful tea party!", R.drawable.ic_account, false),
            Post("Bob The Builder", "Can we fix it? Yes, we can! Check out my latest project.", R.drawable.ic_account, true),
            Post("Charlie Chaplin", "A day without laughter is a day wasted.", R.drawable.ic_account, false),
            Post("Diana Prince", "Working on saving the world, brb.", R.drawable.ic_account, false),
            Post("Edward Scissorhands", "Gardening is quite therapeutic.", R.drawable.ic_account, true)
        )
    }
}

@Composable
fun PostItem(
    post: Post,
    onLikeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = post.profileIconResId),
                    contentDescription = "${post.userName}'s profile picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = post.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onLikeClicked) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant // Consider using theme color for liked state too
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualHomeScreen(navController: NavController) {
    val posts = rememberSamplePosts()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { /* Title text removed */ },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(48.dp), // <<< MODIFIED: Icon size increased
                            tint = MaterialTheme.colorScheme.onSurface 
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(posts) { index, post ->
                PostItem(
                    post = post,
                    onLikeClicked = {
                        val updatedPost = post.copy(isLiked = !post.isLiked)
                        (posts as MutableList<Post>)[index] = updatedPost
                    }
                )
            }
        }
    }
}
