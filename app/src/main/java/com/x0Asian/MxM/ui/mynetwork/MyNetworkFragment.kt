package com.x0Asian.MxM.ui.mynetwork

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.x0Asian.MxM.mynetwork.ConnectionState
import com.x0Asian.MxM.mynetwork.Profile
import com.x0Asian.MxM.mynetwork.RoleType
import com.x0Asian.MxM.mynetwork.sampleMenteeProfile
import com.x0Asian.MxM.mynetwork.sampleMentorProfile

@Composable
private fun FragmentProfileCategoryRow( // <<< RENAMED
    title: String,
    profiles: List<Profile>,
    connectionStates: Map<String, ConnectionState>,
    onProfileConnectClicked: (profileId: String) -> Unit
) {
    if (profiles.isNotEmpty()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(profiles, key = { it.id }) { profile ->
                FragmentProfileCard( // <<< UPDATED CALL
                    profile = profile,
                    connectionState = connectionStates[profile.id] ?: profile.connectionState,
                    onConnectClick = {
                        onProfileConnectClicked(profile.id)
                    }
                )
            }
        }
    }
}

@Composable
fun MyNetworkScreen() { // This is the MyNetworkScreen within MyNetworkFragment.kt
    val mentors = listOf(sampleMentorProfile)
    val mentees = listOf(sampleMenteeProfile)

    var searchQuery by remember { mutableStateOf("") }
    val connectionStates = remember { mutableStateMapOf<String, ConnectionState>() }

    LaunchedEffect(mentors, mentees) {
        (mentors + mentees).forEach { profile ->
            if (!connectionStates.containsKey(profile.id)) {
                connectionStates[profile.id] = profile.connectionState
            }
        }
    }

    val filteredMentors = mentors.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.subjects.any { subj -> subj.contains(searchQuery, ignoreCase = true) }
    }

    val filteredMentees = mentees.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.subjects.any { subj -> subj.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search for Mentor/Mentee") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )

        FragmentProfileCategoryRow( // <<< UPDATED CALL
            title = "Mentors to discover",
            profiles = filteredMentors,
            connectionStates = connectionStates,
            onProfileConnectClicked = { profileId ->
                connectionStates[profileId] = ConnectionState.REQUESTED
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        FragmentProfileCategoryRow( // <<< UPDATED CALL
            title = "Mentees to discover",
            profiles = filteredMentees,
            connectionStates = connectionStates,
            onProfileConnectClicked = { profileId ->
                connectionStates[profileId] = ConnectionState.REQUESTED
            }
        )
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun FragmentProfileCardHeader(profile: Profile) { // <<< RENAMED
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        // Background Image/Color
        when (profile.background) {
            is Color -> Box(modifier = Modifier.fillMaxSize().background(profile.background))
            is String -> AsyncImage(
                model = profile.background,
                contentDescription = "${profile.name}'s background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            is ImageVector -> Image(
                painter = rememberVectorPainter(image = profile.background),
                contentDescription = "${profile.name}'s background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            else -> Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) // Default
        }

        // Profile Picture (Overlapping)
        val profileImageModifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant) // Placeholder bg
            .align(Alignment.BottomCenter)
            .offset(y = 36.dp) // Half of the image size to overlap

        when (profile.profilePicture) {
            is String -> AsyncImage(
                model = profile.profilePicture,
                contentDescription = profile.name,
                contentScale = ContentScale.Crop,
                modifier = profileImageModifier
            )
            is ImageVector -> Image(
                painter = rememberVectorPainter(image = profile.profilePicture),
                contentDescription = profile.name,
                contentScale = ContentScale.Crop,
                modifier = profileImageModifier
            )
            else -> Image( // Fallback default
                painter = rememberVectorPainter(image = Icons.Filled.Person),
                contentDescription = profile.name,
                contentScale = ContentScale.Crop,
                modifier = profileImageModifier
            )
        }
    }
}

@Composable
private fun FragmentProfileCardSubjects(subjects: List<String>, modifier: Modifier = Modifier) { // <<< RENAMED
    Column(
        modifier = modifier.padding(horizontal = 12.dp), // Apply modifier here for weight
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val subjectsToShow = subjects.take(2)
        subjectsToShow.forEach { subject ->
            Text(
                text = subject,
                fontSize = 12.sp,
                color = Color.LightGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
        if (subjects.size > 2) {
            Text(
                text = "+ ${subjects.size - 2} more",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun FragmentProfileCardConnectButton( // <<< RENAMED
    profile: Profile,
    connectionState: ConnectionState,
    onConnectClick: () -> Unit
) {
    Button(
        onClick = onConnectClick,
        enabled = connectionState == ConnectionState.CAN_ADD,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (connectionState == ConnectionState.CAN_ADD) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (connectionState == ConnectionState.CAN_ADD) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Text(
            text = when (connectionState) {
                ConnectionState.CAN_ADD -> if (profile.roleType == RoleType.MENTOR) "Add Mentor" else "Add Mentee"
                ConnectionState.REQUESTED -> "Requested"
            }
        )
    }
}

@Composable
private fun FragmentProfileCard( // <<< RENAMED
    profile: Profile,
    connectionState: ConnectionState,
    onConnectClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(200.dp)
            .height(290.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF202020))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FragmentProfileCardHeader(profile = profile) // <<< UPDATED CALL

            Spacer(modifier = Modifier.height(44.dp)) // Space for overlapping profile pic + a bit more

            Text(
                text = profile.name,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            FragmentProfileCardSubjects(subjects = profile.subjects, modifier = Modifier.weight(1f)) // <<< UPDATED CALL

            FragmentProfileCardConnectButton( // <<< UPDATED CALL
                profile = profile,
                connectionState = connectionState,
                onConnectClick = onConnectClick
            )
        }
    }
}
