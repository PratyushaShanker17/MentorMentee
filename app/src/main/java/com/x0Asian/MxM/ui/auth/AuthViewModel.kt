package com.x0Asian.MxM.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.x0Asian.MxM.data.local.AppDatabase
import com.x0Asian.MxM.data.local.dao.UserProfileDao
import com.x0Asian.MxM.data.models.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.Date
import java.util.UUID

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userProfileDao: UserProfileDao = AppDatabase.getDatabase(application).userProfileDao()

    private val _currentUserProfile = MutableStateFlow<UserProfile?>(null)
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile

    // _userProfile might be redundant if _currentUserProfile serves the same purpose.
    // Consolidating to use _currentUserProfile primarily for the logged-in user.
    // If a different profile needs to be loaded temporarily, a separate StateFlow could be used.
    val userProfile: StateFlow<UserProfile?> = _currentUserProfile // Keep this for now, mirrors currentUserProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        Log.d("AuthVM", "AuthViewModel initialized with Room database.")
        // Optionally, try to load a persisted session here if you implement session management
    }

    // Basic SHA-256 Hashing. IMPORTANT: For production, use a stronger, salted hashing algorithm (e.g., Argon2, scrypt, bcrypt).
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun signUpUserLocally(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("AuthVM", "Attempting Room sign up for: $email")

            try {
                val existingUser = userProfileDao.getUserProfileByEmail(email)
                if (existingUser != null) {
                    _error.value = "Email already registered."
                    _isLoading.value = false
                    return@launch
                }

                val newUserId = UUID.randomUUID().toString()
                val hashedPassword = hashPassword(password)
                val newUser = UserProfile(
                    userId = newUserId,
                    email = email,
                    hashedPassword = hashedPassword,
                    name = displayName,
                    createdAt = Date(),
                    updatedAt = Date()
                )
                userProfileDao.insertUserProfile(newUser)
                _currentUserProfile.value = newUser
                Log.d("AuthVM", "Room sign up successful for: $email, userId: $newUserId")
            } catch (e: Exception) {
                _error.value = e.message ?: "Registration failed"
                Log.e("AuthVM", "Room registration failed", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signInUserLocally(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("AuthVM", "Attempting Room sign in for: $email")

            try {
                val user = userProfileDao.getUserProfileByEmail(email)
                if (user != null) {
                    val hashedPassword = hashPassword(password)
                    if (user.hashedPassword == hashedPassword) {
                        _currentUserProfile.value = user
                        Log.d("AuthVM", "Room sign in successful for: $email")
                    } else {
                        _error.value = "Invalid email or password."
                        Log.w("AuthVM", "Room sign in failed for: $email - password mismatch")
                    }
                } else {
                    _error.value = "Invalid email or password."
                    Log.w("AuthVM", "Room sign in failed for: $email - user not found")
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Sign in failed"
                Log.e("AuthVM", "Room sign in error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("AuthVM", "Attempting to load Room user profile for UID: $userId")
            try {
                // If this is for the currently logged-in user, observe it directly
                // For loading other profiles, you might handle it differently
                userProfileDao.getUserProfileById(userId).collect {
                    _currentUserProfile.value = it // Assuming this is always for the current user for now
                    Log.d("AuthVM", "Room user profile loaded for UID: $userId, Data: $it")
                }
            } catch (e: Exception) {
                _error.value = "Failed to load profile: ${e.message}"
                Log.e("AuthVM", "Room loadUserProfile error for $userId", e)
            } finally {
                _isLoading.value = false // This might be tricky with a collecting Flow
            }
        }
    }

    fun updateUserProfile(updatedProfileData: UserProfile) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("AuthVM", "Attempting to update Room user profile for UID: ${updatedProfileData.userId}")

            try {
                val currentUser = userProfileDao.getUserProfileById(updatedProfileData.userId).firstOrNull()
                if (currentUser != null) {
                    // Create a new UserProfile object ensuring the hashed password is preserved
                    // and only relevant fields are updated from updatedProfileData.
                    val profileToUpdate = currentUser.copy(
                        name = updatedProfileData.name,
                        profileImageUrl = updatedProfileData.profileImageUrl,
                        mentorSkills = updatedProfileData.mentorSkills,
                        mentorExperience = updatedProfileData.mentorExperience,
                        menteeSkills = updatedProfileData.menteeSkills,
                        menteeExperience = updatedProfileData.menteeExperience,
                        updatedAt = Date()
                        // IMPORTANT: Do NOT copy hashedPassword from updatedProfileData unless it's a password change operation
                    )
                    userProfileDao.updateUserProfile(profileToUpdate)
                    _currentUserProfile.value = profileToUpdate // Update the local state
                    Log.d("AuthVM", "Room updateUserProfile successful for UID: ${updatedProfileData.userId}")
                } else {
                    _error.value = "Failed to update profile: User not found."
                    Log.e("AuthVM", "Room updateUserProfile: User ${updatedProfileData.userId} not found for update.")
                }
            } catch (e: Exception) {
                _error.value = "Failed to update profile: ${e.message}"
                Log.e("AuthVM", "Room updateUserProfile error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        Log.d("AuthVM", "Signing out.")
        _currentUserProfile.value = null
        // Clear any other session-related data if necessary
    }

    fun clearError() {
        _error.value = null
    }
}
