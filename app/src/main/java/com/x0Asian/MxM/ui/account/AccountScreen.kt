package com.x0Asian.MxM.ui.account

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
// import androidx.compose.ui.graphics.Color // <<< REMOVED
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// import androidx.navigation.NavController // <<< REMOVED
import com.x0Asian.MxM.ui.navigation.Screen // For TopAppBar title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualAccountScreen() { // <<< MODIFIED: navController parameter removed
    var mentorSkills by remember { mutableStateOf("") }
    var mentorExperience by remember { mutableStateOf("") }
    var menteeSkills by remember { mutableStateOf("") }
    var menteeExperience by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(Screen.Settings.label) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pratyusha Shanker",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = {
                        Log.d("AccountScreen", "Edit clicked. Mentor Skills: $mentorSkills, Mentor Exp: $mentorExperience, Mentee Skills: $menteeSkills, Mentee Exp: $menteeExperience")
                    }) {
                        Text("Edit", color = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mentor Profile Section
            ProfileSectionHeader("Mentor Profile")
            EditableProfileField(
                label = "Skills I want to mentor in:",
                value = mentorSkills,
                onValueChange = { mentorSkills = it },
                hint = "Type skills here..."
            )
            EditableProfileField(
                label = "My Experience:",
                value = mentorExperience,
                onValueChange = { mentorExperience = it },
                hint = "Describe your experience..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mentee Profile Section
            ProfileSectionHeader("Mentee Profile")
            EditableProfileField(
                label = "Skills I want to be mentored in:",
                value = menteeSkills,
                onValueChange = { menteeSkills = it },
                hint = "Type skills here..."
            )
            EditableProfileField(
                label = "Current Experience Level:",
                value = menteeExperience,
                onValueChange = { menteeExperience = it },
                hint = "Describe your current level..."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileSectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(8.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun EditableProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String
) {
    Text(
        text = label,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
        color = MaterialTheme.colorScheme.onSurface
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp),
        placeholder = { Text(hint) }
    )
}
