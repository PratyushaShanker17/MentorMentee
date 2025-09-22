package com.x0Asian.MxM.ui.navigation

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.x0Asian.MxM.ui.auth.AuthViewModel
import com.x0Asian.MxM.ui.auth.LoginScreen
import com.x0Asian.MxM.ui.home.ActualHomeScreen
import com.x0Asian.MxM.ui.mynetwork.MyNetworkScreen
import com.x0Asian.MxM.ui.profile.ActualProfileScreen
import com.x0Asian.MxM.ui.settings.ActualSettingsScreen
import com.x0Asian.MxM.ui.theme.MentorMenteeTheme
import kotlinx.coroutines.launch
import java.util.UUID

// SharedPreferences Constants
private const val PREFERENCES_FILE_KEY = "com.x0Asian.MxM.APP_PREFERENCES"
private const val KEY_DARK_THEME = "dark_theme_enabled"

// Data class for message previews
data class MessagePreview(
    val id: String,
    val senderName: String,
    val lastMessage: String,
    val timestamp: String,
    val profileImageUrl: String? = null, 
    val senderUserId: String 
)

// Data class for individual chat messages
// This should ideally be the Room entity from data.models.ChatMessage
// For now, keeping it here for simplicity of AppNavigation.kt context
// but it should be replaced by the Room entity in a full refactor.
data class ChatMessage(
    val id: String,
    val text: String,
    val senderId: String, 
    val timestamp: Long,  
    val isMine: Boolean   
)

@OptIn(ExperimentalMaterial3Api::class)
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Messages : Screen("messages", "Messages", Icons.Filled.Email)
    object MyNetwork : Screen("mynetwork", "My Network", Icons.Filled.People)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
    object Profile : Screen("profile", "Profile", Icons.Filled.AccountCircle)
    object Conversation : Screen("conversation/{userId}", "Conversation", Icons.Filled.Email) {
        fun createRoute(userId: String) = "conversation/$userId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Messages,
    Screen.MyNetwork,
    Screen.Settings
)

val sampleMessages = listOf(
    MessagePreview("msg1", "Olivia Chen", "On my way!", "10:32 AM", profileImageUrl = null, senderUserId = "user_olivia_chen"),
    MessagePreview("msg2", "James Miller", "You: Sounds good, see you there.", "Yesterday", profileImageUrl = null, senderUserId = "user_james_miller"),
    MessagePreview("msg3", "Sophia Lee", "Can we reschedule?", "Mon", profileImageUrl = null, senderUserId = "user_sophia_lee")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageListItem(message: MessagePreview, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (message.profileImageUrl != null) {
                AsyncImage(
                    model = message.profileImageUrl,
                    contentDescription = "${message.senderName} profile picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(message.senderName.firstOrNull()?.uppercase() ?: "U", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = message.senderName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = message.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message.timestamp,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Messages") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(sampleMessages) { message ->
                MessageListItem(message = message) {
                    navController.navigate(Screen.Conversation.createRoute(message.senderUserId))
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val bubbleColor = if (message.isMine) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val textColor = if (message.isMine) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp,
                bottomStart = if (message.isMine) 16.dp else 0.dp,
                bottomEnd = if (message.isMine) 0.dp else 16.dp
            ),
            color = bubbleColor,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(navController: NavController, userId: String?, authViewModel: AuthViewModel?) { // Made authViewModel nullable
    // Safely access authViewModel, providing a placeholder for previews
    val currentUserIdState = authViewModel?.currentUserProfile?.collectAsState()
    val currentUserId = currentUserIdState?.value?.userId ?: "preview_user_id" 

    val conversationPartner = sampleMessages.find { it.senderUserId == userId }
    val conversationPartnerName = conversationPartner?.senderName ?: userId ?: "Conversation"

    val initialMessages = remember(userId, currentUserId, conversationPartner) {
        val messages = mutableListOf<ChatMessage>()
        val partnerId = userId ?: "other_user_preview"
        val now = System.currentTimeMillis()

        if (conversationPartner != null) {
            val lastPreviewMessage = conversationPartner.lastMessage
            val isLastMineInPreview = lastPreviewMessage.startsWith("You: ")
            val actualLastMessageText = if (isLastMineInPreview) lastPreviewMessage.removePrefix("You: ") else lastPreviewMessage

            if (isLastMineInPreview) {
                messages.add(ChatMessage(UUID.randomUUID().toString(), "Just following up on my last message.", partnerId, now - 20000, false))
                messages.add(ChatMessage(UUID.randomUUID().toString(), actualLastMessageText, currentUserId, now - 10000, true))
            } else {
                messages.add(ChatMessage(UUID.randomUUID().toString(), "Reaching out about your last message.", currentUserId, now - 20000, true))
                messages.add(ChatMessage(UUID.randomUUID().toString(), actualLastMessageText, partnerId, now - 10000, false))
            }
        } else {
            messages.add(ChatMessage(UUID.randomUUID().toString(), "Hey, how are you?", partnerId, now - 50000, false))
            messages.add(ChatMessage(UUID.randomUUID().toString(), "I'm good, thanks! What about you?", currentUserId, now - 40000, true))
        }
        messages.sortedBy { it.timestamp }
    }

    val chatMessages: SnapshotStateList<ChatMessage> = remember(userId, initialMessages) {
        mutableStateListOf<ChatMessage>().apply { addAll(initialMessages) }
    }

    var textState by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(conversationPartnerName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    if (textState.isNotBlank()) {
                        val senderForNewMessage = currentUserId // This will be "preview_user_id" in previews
                        val newMessage = ChatMessage(
                            id = UUID.randomUUID().toString(),
                            text = textState,
                            senderId = senderForNewMessage, 
                            timestamp = System.currentTimeMillis(),
                            isMine = true // This might need adjustment if currentUserId is just a placeholder
                        )
                        chatMessages.add(newMessage)
                        textState = ""
                        coroutineScope.launch {
                            if (chatMessages.isNotEmpty()) {
                                lazyListState.animateScrollToItem(chatMessages.size - 1)
                            }
                        }
                        // In a real app with ViewModel, you'd call viewModel.sendMessage(..)
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(chatMessages, key = { it.id }) { message ->
                ChatMessageItem(message = message)
            }
        }
        LaunchedEffect(chatMessages.size) {
            if (chatMessages.isNotEmpty()) {
                lazyListState.scrollToItem(chatMessages.size - 1)
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedIndex = remember(currentRoute) {
        bottomNavItems.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
    }

    Column {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        ) {
            val itemWidth = maxWidth / bottomNavItems.size
            val indicatorOffset: Dp by animateDpAsState(
                targetValue = itemWidth * selectedIndex,
                label = "indicatorOffset"
            )

            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .fillMaxHeight()
                    .offset(x = indicatorOffset)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            bottomNavItems.forEachIndexed { index, screen ->
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = screen.label) },
                    label = { Text(screen.label) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = screen.route != Screen.Settings.route
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun MainAppScreen() {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
    }

    val systemTheme = isSystemInDarkTheme()
    val initialDarkTheme = remember {
        sharedPreferences.getBoolean(KEY_DARK_THEME, systemTheme)
    }
    var isDarkTheme by remember { mutableStateOf(initialDarkTheme) }

    LaunchedEffect(isDarkTheme) {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_DARK_THEME, isDarkTheme)
            apply()
        }
    }

    val authViewModel: AuthViewModel = viewModel() // Correctly instantiated for the live app
    val currentUser by authViewModel.currentUserProfile.collectAsState()

    MentorMenteeTheme(darkTheme = isDarkTheme) {
        if (currentUser == null) {
            LoginScreen(authViewModel = authViewModel)
        } else {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val showBottomBar = currentRoute !in listOf(Screen.Profile.route, Screen.Conversation.route)

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        BottomNavigationBar(navController = navController)
                    }
                }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { newThemeState ->
                        isDarkTheme = newThemeState
                    },
                    authViewModel = authViewModel // Passed to AppNavHost
                )
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    authViewModel: AuthViewModel // Received from MainAppScreen
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { ActualHomeScreen(navController) }
        composable(Screen.Messages.route) { MessagesScreen(navController) }
        composable(Screen.MyNetwork.route) { MyNetworkScreen(navController) }
        composable(Screen.Settings.route) {
            ActualSettingsScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                authViewModel = authViewModel
            )
        }
        composable(Screen.Profile.route) { ActualProfileScreen(navController) }
        composable(
            route = Screen.Conversation.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            ConversationScreen(
                navController = navController,
                userId = backStackEntry.arguments?.getString("userId"),
                authViewModel = authViewModel // Passed down from AppNavHost
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MentorMenteeTheme {
        MainAppScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun MessagesScreenPreview() {
    MentorMenteeTheme {
        MessagesScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun ConversationScreenPreview() {
    MentorMenteeTheme {
        // val dummyAuthViewModel = AuthViewModel() // This line is removed
        // Preview with a user whose last message was from them
        // ConversationScreen(navController = rememberNavController(), userId = "user_olivia_chen", authViewModel = null)
        // Preview with a user whose last message was from "You:"
         ConversationScreen(navController = rememberNavController(), userId = "user_james_miller", authViewModel = null)
    }
}
