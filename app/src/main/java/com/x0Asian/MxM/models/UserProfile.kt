package com.x0Asian.MxM.models

data class UserProfile(
    val uid: String = "", // User's Firebase Auth UID
    val email: String? = null,
    var displayName: String? = null,
    var photoUrl: String? = null, // Will be updated from Cloud Storage
    var bio: String? = null
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this("", null, null, null, null)
}
