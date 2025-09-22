package com.x0Asian.MxM.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey
    var userId: String = "", // User ID
    var email: String = "", // User's email address
    var hashedPassword: String = "", // Stores the hashed password

    var name: String = "", // User's display name
    var profileImageUrl: String = "", // Path to the local profile image or a remote URL

    var mentorSkills: String = "", // Skills the user wants to mentor in
    var mentorExperience: String = "", // Description of mentor experience

    var menteeSkills: String = "", // Skills the user wants to be mentored in
    var menteeExperience: String = "", // Description of mentee's current experience level

    var createdAt: Date? = null, // To store creation time
    var updatedAt: Date? = null  // To store last update time
)
