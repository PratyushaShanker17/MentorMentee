package com.example.mentormentee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// import androidx.compose.foundation.layout.fillMaxSize // No longer directly needed here
// import androidx.compose.foundation.layout.padding // No longer directly needed here
// import androidx.compose.material3.Scaffold // No longer directly needed here
import androidx.compose.material3.Text // Keep for Greeting function
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mentormentee.ui.theme.MentorMenteeTheme
import com.x0Asian.MxM.ui.navigation.MainAppScreen // Added this import

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MentorMenteeTheme {
                MainAppScreen() // Changed to call MainAppScreen
            }
        }
    }
}

// Greeting function can remain for previews or other uses if needed
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MentorMenteeTheme {
        Greeting("Android")
    }
}