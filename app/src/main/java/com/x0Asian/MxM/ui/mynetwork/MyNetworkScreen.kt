package com.x0Asian.MxM.ui.mynetwork

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.x0Asian.MxM.mynetwork.ConnectionState // Assuming this enum exists
import com.x0Asian.MxM.mynetwork.Profile // Assuming this data class exists
import com.x0Asian.MxM.mynetwork.RoleType // Assuming this enum exists
// Imports for original sampleMentorProfile and sampleMenteeProfile are no longer strictly needed
// as we will redefine them locally for this screen's sample data.
import com.x0Asian.MxM.ui.navigation.Screen

// Redefined sample profiles with generic names and professional subjects
val localSampleMentorProfile = Profile(
    id = "mentor_john_doe",
    name = "John Doe",
    subjects = listOf("Software Engineering", "System Design"),
    background = Color.Blue.copy(alpha = 0.3f),
    profilePicture = Icons.Filled.Person, // Placeholder icon
    roleType = RoleType.MENTOR,
    connectionState = ConnectionState.CAN_ADD
)

val localSampleMenteeProfile = Profile(
    id = "mentee_jane_smith",
    name = "Jane Smith",
    subjects = listOf("Web Development", "React"),
    background = Color.Red.copy(alpha = 0.3f),
    profilePicture = Icons.Filled.Person, // Placeholder icon
    roleType = RoleType.MENTEE,
    connectionState = ConnectionState.CAN_ADD
)

val updatedNewMentorProfiles = listOf(
    Profile(id = "mentor_michael_brown", name = "Michael Brown", subjects = listOf("Data Science", "Python Programming"), background = Color.Magenta.copy(alpha = 0.3f), profilePicture = Icons.Filled.Person, roleType = RoleType.MENTOR),
    Profile(id = "mentor_emily_davis", name = "Emily Davis", subjects = listOf("UX Design", "User Research"), background = Color.Cyan.copy(alpha = 0.3f), profilePicture = Icons.Filled.Person, roleType = RoleType.MENTOR),
    Profile(id = "mentor_david_wilson", name = "David Wilson", subjects = listOf("Cloud Computing (AWS)", "Cybersecurity"), background = Color.Yellow.copy(alpha = 0.3f), profilePicture = Icons.Filled.Person, roleType = RoleType.MENTOR),
    Profile(id = "mentor_sarah_jones", name = "Sarah Jones", subjects = listOf("Product Management", "Agile Methodologies"), background = Color.Green.copy(alpha = 0.3f), profilePicture = Icons.Filled.Person, roleType = RoleType.MENTOR)
)

val updatedNewMenteeProfiles = listOf(
    Profile(id = "mentee_chris_taylor", name = "Chris Taylor", subjects = listOf("Mobile App Development", "SwiftUI"), background = Color.Gray.copy(alpha = 0.3f), profilePicture = Icons.Filled.Person, roleType = RoleType.MENTEE),
    Profile(id = "mentee_jessica_rodriguez", name = "Jessica Rodriguez", subjects = listOf("Digital Marketing", "SEO Basics"), background = Color.LightGray.copy(alpha = 0.3f), profilePicture = Icons.Filled.Person, roleType = RoleType.MENTEE),
    Profile(id = "mentee_kevin_lee", name = "Kevin Lee", subjects = listOf("Data Analysis with SQL", "Tableau"), background = Color.DarkGray.copy(alpha = 0.3f), profilePicture = Icons.Filled.Person, roleType = RoleType.MENTEE),
    Profile(id = "mentee_ashley_white", name = "Ashley White", subjects = listOf("Getting Started with Java", "Object-Oriented Programming"), background = Color.Blue.copy(alpha = 0.2f), profilePicture = Icons.Filled.Person, roleType = RoleType.MENTEE)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNetworkScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { /* Title text can be added later if needed */ },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(48.dp),
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
        val mentors = listOf(localSampleMentorProfile) + updatedNewMentorProfiles
        val mentees = listOf(localSampleMenteeProfile) + updatedNewMenteeProfiles

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
                .padding(innerPadding) 
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

            ProfileCategoryRow(
                title = "Mentors to discover",
                profiles = filteredMentors,
                connectionStates = connectionStates,
                onProfileConnectClicked = { profileId ->
                    connectionStates[profileId] = ConnectionState.REQUESTED
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileCategoryRow(
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
}

@Composable
fun ProfileCategoryRow(
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
                ProfileCard(
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
private fun profileCardHeader(profile: Profile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        when (val bg = profile.background) {
            is Color -> Box(modifier = Modifier.fillMaxSize().background(bg))
            is String -> AsyncImage(
                model = bg,
                contentDescription = "${profile.name}'s background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            is ImageVector -> Image(
                painter = rememberVectorPainter(image = bg),
                contentDescription = "${profile.name}'s background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            else -> Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))
        }

        val profileImageModifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .align(Alignment.BottomCenter)
            .offset(y = 36.dp)

        when (val pic = profile.profilePicture) {
            is String -> AsyncImage(
                model = pic,
                contentDescription = profile.name,
                contentScale = ContentScale.Crop,
                modifier = profileImageModifier
            )
            is ImageVector -> Image(
                painter = rememberVectorPainter(image = pic),
                contentDescription = profile.name,
                contentScale = ContentScale.Crop,
                modifier = profileImageModifier
            )
            else -> Image(
                painter = rememberVectorPainter(image = Icons.Filled.Person),
                contentDescription = profile.name,
                contentScale = ContentScale.Crop,
                modifier = profileImageModifier
            )
        }
    }
}

@Composable
private fun profileCardSubjects(subjects: List<String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
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
private fun profileCardConnectButton(
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
fun ProfileCard(
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
            profileCardHeader(profile = profile)

            Spacer(modifier = Modifier.height(44.dp))

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

            profileCardSubjects(subjects = profile.subjects, modifier = Modifier.weight(1f))

            profileCardConnectButton(
                profile = profile,
                connectionState = connectionState,
                onConnectClick = onConnectClick
            )
        }
    }
}
