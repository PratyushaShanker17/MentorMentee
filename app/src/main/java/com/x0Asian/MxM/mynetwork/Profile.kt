package com.x0Asian.MxM.mynetwork

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

enum class ConnectionState {
    CAN_ADD,
    REQUESTED
}

data class Profile(
    val id: String,
    val name: String,
    val profilePicture: Any, // Can be String (URL) or ImageVector for default
    val background: Any,     // Can be String (URL) or Color for default
    val subjects: List<String>,
    val roleType: RoleType, // To distinguish if this card is for a Mentor or Mentee
    var connectionState: ConnectionState = ConnectionState.CAN_ADD
)

enum class RoleType {
    MENTOR,
    MENTEE
}

// Example placeholder data - this would typically come from a ViewModel
val sampleMentorProfile = Profile(
    id = "1",
    name = "William fictive Brown", // Example name
    profilePicture = Icons.Filled.Person, // Default person icon
    background = Color.DarkGray, // Default background color
    subjects = listOf("Android Development", "Kotlin", "Jetpack Compose", "UI/UX"),
    roleType = RoleType.MENTOR
)

val sampleMenteeProfile = Profile(
    id = "2",
    name = "Erica fictive Truxton", // Example name
    profilePicture = Icons.Filled.Person,
    background = Color.DarkGray,
    subjects = listOf("Machine Learning", "Python"),
    roleType = RoleType.MENTEE
)
