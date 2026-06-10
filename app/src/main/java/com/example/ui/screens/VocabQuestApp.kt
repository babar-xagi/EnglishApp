package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Spring
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.shadow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.data.database.GameResult
import com.example.data.database.VocabularyWord
import com.example.ui.theme.*
import com.example.ui.viewmodel.VocabViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VocabQuestApp(viewModel: VocabViewModel) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsStateWithLifecycle()

    if (!isOnboardingCompleted) {
        LoginScreen(
            onLoginSuccess = { name ->
                viewModel.completeOnboarding(
                    name = name,
                    level = "Intermediate",
                    goals = listOf("USA University Interview", "Daily Fluency"),
                    dailyTime = 30,
                    topics = listOf("AI & Data Science", "University Life")
                )
            },
            onOnboardingComplete = { name, level, goals, dailyTime, topics ->
                viewModel.completeOnboarding(name, level, goals, dailyTime, topics)
            },
            viewModel = viewModel
        )
    } else {
        MainAppLayout(viewModel)
    }
}

// --- DYNAMIC ILLUSTRATIVE DRAWINGS (PHONE 1 & PHONE 2 NO-ASSET EXACT REPLICAS) ---

@Composable
fun UserAvatar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color(0xFFC8E6C9)), // Light green circle
        contentAlignment = Alignment.Center
    ) {
        // Drawing/composing head + shoulders natively
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            // Draw shoulders (green shirt)
            drawOval(
                color = Color(0xFF2E7D32),
                topLeft = androidx.compose.ui.geometry.Offset(width * 0.15f, height * 0.65f),
                size = androidx.compose.ui.geometry.Size(width * 0.7f, height * 0.5f)
            )
            
            // Draw neck
            drawRect(
                color = Color(0xFFFFCC80), // Skin tone
                topLeft = androidx.compose.ui.geometry.Offset(width * 0.4f, height * 0.45f),
                size = androidx.compose.ui.geometry.Size(width * 0.2f, height * 0.2f)
            )
            
            // Draw head
            drawCircle(
                color = Color(0xFFFFCC80), // Skin tone
                radius = width * 0.24f,
                center = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.38f)
            )
            
            // Draw hair
            drawArc(
                color = Color(0xFF212121),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = androidx.compose.ui.geometry.Offset(width * 0.25f, height * 0.14f),
                size = androidx.compose.ui.geometry.Size(width * 0.5f, height * 0.35f)
            )
            // Hair styling locks
            drawCircle(
                color = Color(0xFF212121),
                radius = width * 0.08f,
                center = androidx.compose.ui.geometry.Offset(width * 0.38f, height * 0.22f)
            )
            drawCircle(
                color = Color(0xFF212121),
                radius = width * 0.08f,
                center = androidx.compose.ui.geometry.Offset(width * 0.62f, height * 0.22f)
            )
            
            // Simple eyes
            drawCircle(
                color = Color(0xFF212121),
                radius = width * 0.03f,
                center = androidx.compose.ui.geometry.Offset(width * 0.43f, height * 0.38f)
            )
            drawCircle(
                color = Color(0xFF212121),
                radius = width * 0.03f,
                center = androidx.compose.ui.geometry.Offset(width * 0.57f, height * 0.38f)
            )
            
            // Smiling mouth
            drawArc(
                color = Color(0xFFD32F2F),
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(width * 0.42f, height * 0.42f),
                size = androidx.compose.ui.geometry.Size(width * 0.16f, height * 0.1f)
            )
        }
    }
}

@Composable
fun OpenBookDrawing() {
    Canvas(modifier = Modifier.size(54.dp)) {
        val w = size.width
        val h = size.height
        
        // Background shadow
        drawRoundRect(
            color = Color(0xFFA5D6A7).copy(alpha = 0.5f),
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.1f, h * 0.15f),
            size = androidx.compose.ui.geometry.Size(w * 0.8f, h * 0.7f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
        
        // Open Pages left
        drawRoundRect(
            color = Color.White,
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.12f, h * 0.2f),
            size = androidx.compose.ui.geometry.Size(w * 0.35f, h * 0.55f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
        // Open Pages right
        drawRoundRect(
            color = Color.White,
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.53f, h * 0.2f),
            size = androidx.compose.ui.geometry.Size(w * 0.35f, h * 0.55f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
        
        // Spine center
        drawRect(
            color = Color(0xFF1B5E20),
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.47f, h * 0.17f),
            size = androidx.compose.ui.geometry.Size(w * 0.06f, h * 0.61f)
        )
        
        // Subtle line markings in page
        drawLine(
            color = Color.LightGray,
            start = androidx.compose.ui.geometry.Offset(w * 0.18f, h * 0.32f),
            end = androidx.compose.ui.geometry.Offset(w * 0.42f, h * 0.32f),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.LightGray,
            start = androidx.compose.ui.geometry.Offset(w * 0.18f, h * 0.45f),
            end = androidx.compose.ui.geometry.Offset(w * 0.42f, h * 0.45f),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.LightGray,
            start = androidx.compose.ui.geometry.Offset(w * 0.18f, h * 0.58f),
            end = androidx.compose.ui.geometry.Offset(w * 0.42f, h * 0.58f),
            strokeWidth = 2.dp.toPx()
        )
        
        drawLine(
            color = Color.LightGray,
            start = androidx.compose.ui.geometry.Offset(w * 0.58f, h * 0.32f),
            end = androidx.compose.ui.geometry.Offset(w * 0.82f, h * 0.32f),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.LightGray,
            start = androidx.compose.ui.geometry.Offset(w * 0.58f, h * 0.45f),
            end = androidx.compose.ui.geometry.Offset(w * 0.82f, h * 0.45f),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.LightGray,
            start = androidx.compose.ui.geometry.Offset(w * 0.58f, h * 0.58f),
            end = androidx.compose.ui.geometry.Offset(w * 0.82f, h * 0.58f),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
fun GraduationHatDrawing() {
    Canvas(modifier = Modifier.size(54.dp)) {
        val w = size.width
        val h = size.height
        
        // Bottom hat cap block
        drawRect(
            color = Color(0xFF1E3A8A),
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.32f, h * 0.48f),
            size = androidx.compose.ui.geometry.Size(w * 0.36f, h * 0.22f)
        )
        
        // Mortarboard diamond top
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(w * 0.5f, h * 0.22f) // top
            lineTo(w * 0.85f, h * 0.42f) // right
            lineTo(w * 0.5f, h * 0.62f) // bottom
            lineTo(w * 0.15f, h * 0.42f) // left
            close()
        }
        drawPath(path, color = Color(0xFF0F172A))
        
        // Tassel cord and ribbon on right side
        drawLine(
            color = Color(0xFFF59E0B), // Golden yellow
            start = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.42f),
            end = androidx.compose.ui.geometry.Offset(w * 0.25f, h * 0.52f),
            strokeWidth = 2.dp.toPx()
        )
        drawCircle(
            color = Color(0xFFF59E0B), // Tassel end bobble
            radius = w * 0.05f,
            center = androidx.compose.ui.geometry.Offset(w * 0.23f, h * 0.54f)
        )
    }
}

@Composable
fun IELTSHeadphonesDrawing() {
    Canvas(modifier = Modifier.size(54.dp)) {
        val w = size.width
        val h = size.height
        
        // Main headband arc
        drawArc(
            color = Color(0xFF6B21A8), // Purple
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.2f, h * 0.22f),
            size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.55f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
        )
        
        // Left Ear pad
        drawRoundRect(
            color = Color(0xFF6B21A8),
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.15f, h * 0.45f),
            size = androidx.compose.ui.geometry.Size(w * 0.14f, h * 0.26f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
        
        // Right Ear pad
        drawRoundRect(
            color = Color(0xFF6B21A8),
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.71f, h * 0.45f),
            size = androidx.compose.ui.geometry.Size(w * 0.14f, h * 0.26f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
        
        // Microphone loop drawing
        drawLine(
            color = Color(0xFF9333EA),
            start = androidx.compose.ui.geometry.Offset(w * 0.22f, h * 0.6f),
            end = androidx.compose.ui.geometry.Offset(w * 0.4f, h * 0.78f),
            strokeWidth = 3.dp.toPx()
        )
        drawCircle(
            color = Color(0xFF9333EA),
            radius = w * 0.045f,
            center = androidx.compose.ui.geometry.Offset(w * 0.42f, h * 0.8f)
        )
    }
}

@Composable
fun InterviewBriefcaseDrawing() {
    Canvas(modifier = Modifier.size(54.dp)) {
        val w = size.width
        val h = size.height
        
        // Handle on top
        drawArc(
            color = Color(0xFF78350F), // Dark leather brown
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.38f, h * 0.18f),
            size = androidx.compose.ui.geometry.Size(w * 0.24f, h * 0.18f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
        )
        
        // Main briefcase brown container body
        drawRoundRect(
            color = Color(0xFF92400E), // Leather brown
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.15f, h * 0.32f),
            size = androidx.compose.ui.geometry.Size(w * 0.7f, h * 0.46f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
        
        // Horizontal decorative band
        drawLine(
            color = Color(0xFF78350F),
            start = androidx.compose.ui.geometry.Offset(w * 0.15f, h * 0.46f),
            end = androidx.compose.ui.geometry.Offset(w * 0.85f, h * 0.46f),
            strokeWidth = 3.dp.toPx()
        )
        
        // Golden brass lock/clasp in middle
        drawRect(
            color = Color(0xFFF59E0B), // Gold
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.45f, h * 0.42f),
            size = androidx.compose.ui.geometry.Size(w * 0.1f, h * 0.12f)
        )
        drawCircle(
            color = Color(0xFF0F172A), // Black center keyhole
            radius = w * 0.015f,
            center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.48f)
        )
    }
}

@Composable
fun GridActionCard(
    title: String,
    containerColor: Color,
    borderColor: Color,
    arrowColor: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon layout
            Box(
                modifier = Modifier.size(54.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            
            // Title
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color(0xFF1E293B)
            )
            
            // Arrow indicator circle
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(arrowColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowOutward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
fun LoginIllustration() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        // Left: Potted Plant
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 12.dp)
        ) {
            // Rounded leaves stack with offsets
            Box(modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .offset(y = (-16).dp)
                        .size(24.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp))
                        .background(Color(0xFF2E7D32))
                )
                Box(
                    modifier = Modifier
                        .offset(x = (-14).dp, y = 4.dp)
                        .size(22.dp)
                        .clip(RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp))
                        .background(Color(0xFF4CAF50))
                )
                Box(
                    modifier = Modifier
                        .offset(x = 14.dp, y = 4.dp)
                        .size(22.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp))
                        .background(Color(0xFF1B5E20))
                )
            }
            // Terracotta Pot
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .background(Color(0xFF8D6E63))
            )
        }

        // Center: Stack of Books
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            // Cream top book
            Box(
                modifier = Modifier
                    .width(86.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFF9C4)) // Lighter warm yellow
                    .border(1.5.dp, Color(0xFFFBC02D), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(10.dp)
                        .background(Color(0xFFFBC02D))
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            // Green "ENGLISH" middle book
            Box(
                modifier = Modifier
                    .width(112.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF386B40))
                    .border(1.5.dp, Color(0xFF1B5E20), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ENGLISH",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            // Red bottom book
            Box(
                modifier = Modifier
                    .width(124.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFD32F2F))
                    .border(1.5.dp, Color(0xFF990000), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(14.dp)
                        .background(Color(0xFF990000))
                )
            }
        }

        // Right: Green Cup
        Box(
            modifier = Modifier
                .padding(start = 12.dp, bottom = 4.dp)
                .size(46.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            // Handle
            Box(
                modifier = Modifier
                    .offset(x = 22.dp, y = (-4).dp)
                    .size(20.dp)
                    .border(3.dp, Color(0xFF386B40), CircleShape)
            )
            // Body
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                    .background(Color(0xFF386B40)),
                contentAlignment = Alignment.Center
            ) {
                Text("Aa", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onOnboardingComplete: (String, String, List<String>, Int, List<String>) -> Unit,
    viewModel: VocabViewModel
) {
    var screenState by remember { mutableStateOf("login") } // "login" or "onboarding"

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    if (screenState == "onboarding") {
        OnboardingScreen(onGetStarted = { customName, level, goals, dailyTime, topics ->
            onOnboardingComplete(customName, level, goals, dailyTime, topics)
        })
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFF7FAF7) // soft light canvas matches Phone 1
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Aa Speechbubble Logo exact replica
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF386B40)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .border(2.5.dp, Color.White, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Aa",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // VocabQuest Title
                    Text(
                        text = "VocabQuest",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            color = Color(0xFF1B5E20)
                        )
                    )

                    // Subtitle
                    Text(
                        text = "Improve English from\nBasic to Advanced",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dynamic Illustrative drawing
                    LoginIllustration()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name input field Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF386B40).copy(alpha = 0.7f),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            BasicTextField(
                                value = name,
                                onValueChange = { name = it },
                                modifier = Modifier.fillMaxWidth().testTag("login_name_input"),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Medium
                                ),
                                decorationBox = { innerTextField ->
                                    if (name.isEmpty()) {
                                        Text(
                                            "Name",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = Color.Gray.copy(alpha = 0.7f)
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }

                    // Password input field Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF386B40).copy(alpha = 0.7f),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            BasicTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier.weight(1f).testTag("login_password_input"),
                                singleLine = true,
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Medium
                                ),
                                decorationBox = { innerTextField ->
                                    if (password.isEmpty()) {
                                        Text(
                                            "Password",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = Color.Gray.copy(alpha = 0.7f)
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password view",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Login Button in exact deep green
                    Button(
                        onClick = {
                            val targetName = name.ifBlank { "Babar" }
                            onLoginSuccess(targetName)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("login_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                    ) {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    // OR divider
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color.Gray.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "OR",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color.Gray.copy(alpha = 0.3f)
                        )
                    }

                    // Create Account text button
                    TextButton(
                        onClick = { screenState = "onboarding" },
                        modifier = Modifier.testTag("create_account_button")
                    ) {
                        Text(
                            text = "Create Account",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

// --- ONBOARDING SCREEN ---

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(onGetStarted: (String, String, List<String>, Int, List<String>) -> Unit) {
    var name by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("Intermediate") }
    var dailyTimeLimit by remember { mutableStateOf(30) }
    
    val goalsList = listOf("USA University Interview", "Visa Interview", "Duolingo Test", "Daily Fluency", "Scholarship")
    val selectedGoals = remember { mutableStateListOf("USA University Interview", "Daily Fluency") }

    val topicsList = listOf("AI & Data Science", "Technology", "University Life", "USA Study", "Career Goals", "Academic Writing")
    val selectedTopics = remember { mutableStateListOf("AI & Data Science", "University Life") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // App visual header with depth overlapping layers
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.School,
                    contentDescription = "VocabQuest Logo",
                    tint = Color.White,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "VocabQuest",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Master fluent English for interviews & university life through gamified context!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Form container Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Customize Your Quest",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Name input
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("What's your name?") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("name_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
                        )
                    )

                    // Target Level Selector
                    Column {
                        Text(
                            text = "My Current English Level",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Beginner", "Intermediate", "Advanced").forEach { lvl ->
                                val selected = selectedLevel == lvl
                                FilterChip(
                                    selected = selected,
                                    onClick = { selectedLevel = lvl },
                                    label = { Text(lvl) },
                                    modifier = Modifier.weight(1f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }
                    }

                    // Daily Committment Slider
                    Column {
                        Text(
                            text = "Daily Time Commitment: $dailyTimeLimit Minutes",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(15, 30, 60).forEach { mins ->
                                val selected = dailyTimeLimit == mins
                                FilterChip(
                                    selected = selected,
                                    onClick = { dailyTimeLimit = mins },
                                    label = { Text("$mins min/day") },
                                    modifier = Modifier.weight(1f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }
                    }

                    // Goals selector
                    Column {
                        Text(
                            text = "My English Goals (Select multiple)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            goalsList.forEach { goal ->
                                val selected = selectedGoals.contains(goal)
                                FilterChip(
                                    selected = selected,
                                    onClick = {
                                        if (selected) selectedGoals.remove(goal) else selectedGoals.add(goal)
                                    },
                                    label = { Text(goal, fontSize = 12.sp) },
                                    leadingIcon = if (selected) {
                                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                    } else null
                                )
                            }
                        }
                    }

                    // Favorite topics
                    Column {
                        Text(
                            text = "My Interests (Select favorite topics)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            topicsList.forEach { topic ->
                                val selected = selectedTopics.contains(topic)
                                FilterChip(
                                    selected = selected,
                                    onClick = {
                                        if (selected) selectedTopics.remove(topic) else selectedTopics.add(topic)
                                    },
                                    label = { Text(topic, fontSize = 12.sp) },
                                    leadingIcon = if (selected) {
                                        { Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                    } else null
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onGetStarted(name, selectedLevel, selectedGoals, dailyTimeLimit, selectedTopics)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("get_started_button"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Start Your VocabQuest", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}


// --- STUNNING 3D FLASH CARD VIEW ---
@Composable
fun FlashCardView(
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var rotated by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "CardFlipRotation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12 * density
            }
            .clickable { rotated = !rotated },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                frontContent()
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f }
                ) {
                    backContent()
                }
            }
        }
    }
}

// --- NATIVE STUDY REMINDER ALARM DIALOG ---
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudyReminderDialog(viewModel: VocabViewModel, onDismiss: () -> Unit) {
    val currentHour by viewModel.reminderHour.collectAsStateWithLifecycle()
    val currentMin by viewModel.reminderMinute.collectAsStateWithLifecycle()
    val currentDays by viewModel.reminderDays.collectAsStateWithLifecycle()
    val isEnabled by viewModel.isReminderEnabled.collectAsStateWithLifecycle()

    var alarmHour by remember { mutableStateOf(currentHour) }
    var alarmMin by remember { mutableStateOf(currentMin) }
    val alarmDays = remember { mutableStateListOf<String>().apply { addAll(currentDays) } }

    val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Alarm, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Study Reminder", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Establish constant vocabulary habits! This will prompt a custom, high-fidelity reminder when it's time.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                
                Text("Reminder Hour", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    IconButton(
                        onClick = { alarmHour = (alarmHour - 1 + 24) % 24 },
                        modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    Text(
                        text = String.format("%02d", alarmHour),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = { alarmHour = (alarmHour + 1) % 24 },
                        modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }

                Text("Minute Offset", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    IconButton(
                        onClick = { alarmMin = (alarmMin - 5 + 60) % 60 },
                        modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    Text(
                        text = String.format("%02d", alarmMin),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = { alarmMin = (alarmMin + 5) % 60 },
                        modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }

                Text("Days Active", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                FlowRow(
                    maxItemsInEachRow = 4,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    weekdays.forEach { day ->
                        val isSelected = alarmDays.contains(day)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    if (isSelected) alarmDays.remove(day) else alarmDays.add(day)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                day.take(1),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Button(
                    onClick = {
                        viewModel.simulateNotificationTrigger()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Trigger Test Alarm (Animation)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.saveReminder(alarmHour, alarmMin, alarmDays.toList())
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Reminder")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// --- MAIN APP LAYOUT (NavigationBar + Scaffold) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppLayout(viewModel: VocabViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isDark by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val isReminderEnabled by viewModel.isReminderEnabled.collectAsStateWithLifecycle()
    val notificationMsg by viewModel.inAppNotificationMessage.collectAsStateWithLifecycle()
    
    var showReminderDialog by remember { mutableStateOf(false) }

    if (showReminderDialog) {
        StudyReminderDialog(viewModel = viewModel, onDismiss = { showReminderDialog = false })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "VocabQuest",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showReminderDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Schedule Study Reminder",
                                tint = if (isReminderEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        IconButton(onClick = { viewModel.toggleTheme() }) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Light/Dark Theme",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(if (selectedTab == 0) Icons.Filled.Dashboard else Icons.Outlined.Dashboard, contentDescription = "Dashboard") },
                        label = { Text("Quest") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(if (selectedTab == 1) Icons.Filled.MenuBook else Icons.Outlined.MenuBook, contentDescription = "Reading") },
                        label = { Text("Mission") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(if (selectedTab == 2) Icons.Filled.Class else Icons.Outlined.Class, contentDescription = "Notebook") },
                        label = { Text("Notebook") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(if (selectedTab == 3) Icons.Filled.VideogameAsset else Icons.Outlined.VideogameAsset, contentDescription = "Games") },
                        label = { Text("Games") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        icon = { Icon(if (selectedTab == 4) Icons.Filled.SupervisedUserCircle else Icons.Outlined.SupervisedUserCircle, contentDescription = "Prep") },
                        label = { Text("Coach") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> DashboardScreen(viewModel, onGoToTab = { selectedTab = it })
                    1 -> MissionInputScreen(viewModel)
                    2 -> VocabularyNotebookScreen(viewModel)
                    3 -> GamesScreen(viewModel)
                    4 -> InterviewCoachScreen(viewModel)
                }
            }
        }

        // Custom, beautifully animated in-app spring reminder notification
        AnimatedVisibility(
            visible = notificationMsg != null,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            ) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp, start = 16.dp, end = 16.dp)
                .zIndex(999f)
        ) {
            notificationMsg?.let { msg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Alarm trigger",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "VocabQuest Study Alarm ⏰",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                msg,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { viewModel.inAppNotificationMessage.value = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss reminder", tint = Color.Gray, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}


// --- TAB 1: DASHBOARD (HOME) ---
@Composable
fun DashboardScreen(
    viewModel: VocabViewModel,
    onGoToTab: (Int) -> Unit
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val wordsList by viewModel.vocabWords.collectAsStateWithLifecycle()
    val speakingPractices by viewModel.speakingPractices.collectAsStateWithLifecycle()
    
    // Checklist interactive states (fallback to custom remember state so users can also click to toggle)
    var isLearnGoalCompleted by remember(wordsList) { mutableStateOf(wordsList.size >= 5) }
    var isSpeakingGoalCompleted by remember(speakingPractices) { mutableStateOf(speakingPractices.isNotEmpty()) }
    var isReviewGoalCompleted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF7FAF7)) // soft off-white canvas
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header greeting
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cartoon Boy profile avatar
                UserAvatar()
                
                Column {
                    Text(
                        "Good Morning,",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        "${profile?.name ?: "Babar"} 👋",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1E293B)
                        )
                    )
                }
            }
            
            // Notification bell with green badge dot
            Box {
                IconButton(onClick = { /* Handle notifications */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(28.dp)
                    )
                }
                // Little green dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF386B40))
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = (4).dp)
                )
            }
        }

        // 2. Streak Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flame icon round wrapper
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF3E0)), // light orange
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔥", fontSize = 24.sp)
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "7 Day Streak",
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                "Keep it up!",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        
                        Text(
                            "120 XP",
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF386B40) // Green XP
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Streak Progress Bar in green
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.7f) // 7/10 is 70% progress matches
                                .clip(CircleShape)
                                .background(Color(0xFF386B40))
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Text(
                        "7 / 10",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }

        // 3. Grid of 4 Cards (2x2 Column layout)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                // Class/Book card: English
                GridActionCard(
                    title = "English",
                    containerColor = Color(0xFFE8F5E9),
                    borderColor = Color(0xFFC8E6C9),
                    arrowColor = Color(0xFF2E7D32),
                    icon = { OpenBookDrawing() },
                    onClick = { onGoToTab(2) }, // Goes to Notebook (Learn)
                    modifier = Modifier.weight(1f)
                )
                // Graduation/Quiz exam card: DET
                GridActionCard(
                    title = "DET",
                    containerColor = Color(0xFFE3F2FD),
                    borderColor = Color(0xFFBBDEFB),
                    arrowColor = Color(0xFF1A73E8),
                    icon = { GraduationHatDrawing() },
                    onClick = { onGoToTab(3) }, // Goes to Games (Exam prep quiz)
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                // Headset practice listening/speaking: IELTS
                GridActionCard(
                    title = "IELTS",
                    containerColor = Color(0xFFF3E5F5),
                    borderColor = Color(0xFFE1BEE7),
                    arrowColor = Color(0xFF8E24AA),
                    icon = { IELTSHeadphonesDrawing() },
                    onClick = { onGoToTab(1) }, // Goes to Mission (Speaking practice)
                    modifier = Modifier.weight(1f)
                )
                // Briefcase Interview coach card: Interview
                GridActionCard(
                    title = "Interview",
                    containerColor = Color(0xFFFFF3E0),
                    borderColor = Color(0xFFFFE0B2),
                    arrowColor = Color(0xFFE65100),
                    icon = { InterviewBriefcaseDrawing() },
                    onClick = { onGoToTab(4) }, // Goes to Coach (Profile/Admissions mock)
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 4. Today's Goal Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🎯 Today's Goal",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1E293B)
                    )
                )
            }
            TextButton(onClick = { /* Handle Goals details */ }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "View All",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF386B40)
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(">", fontWeight = FontWeight.ExtraBold, color = Color(0xFF386B40))
                }
            }
        }

        // 5. Checklist rows
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, Color(0xFFF1F5F9))
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Goal 1 Row
                GoalItemRow(
                    title = "Learn 5 words",
                    isCompleted = isLearnGoalCompleted,
                    avatarBackground = Color(0xFFE8F5E9),
                    iconColor = Color(0xFF2E7D32),
                    imageVector = Icons.Default.MenuBook,
                    onClick = { isLearnGoalCompleted = !isLearnGoalCompleted }
                )
                
                HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(horizontal = 12.dp))
                
                // Goal 2 Row
                GoalItemRow(
                    title = "1 speaking practice",
                    isCompleted = isSpeakingGoalCompleted,
                    avatarBackground = Color(0xFFE3F2FD),
                    iconColor = Color(0xFF1A73E8),
                    imageVector = Icons.Default.Mic,
                    onClick = { isSpeakingGoalCompleted = !isSpeakingGoalCompleted }
                )
                
                HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(horizontal = 12.dp))
                
                // Goal 3 Row
                GoalItemRow(
                    title = "Review old words",
                    isCompleted = isReviewGoalCompleted,
                    avatarBackground = Color(0xFFFFF3E0),
                    iconColor = Color(0xFFE65100),
                    imageVector = Icons.Default.Refresh,
                    onClick = { isReviewGoalCompleted = !isReviewGoalCompleted }
                )
            }
        }
    }
}

@Composable
fun GoalItemRow(
    title: String,
    isCompleted: Boolean,
    avatarBackground: Color,
    iconColor: Color,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Circle avatar wrapper
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(avatarBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )
            )
        }
        
        // Circular checkbox widget
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .border(2.dp, if (isCompleted) Color(0xFF386B40) else Color.Gray.copy(alpha = 0.5f), CircleShape)
                .background(if (isCompleted) Color(0xFF386B40) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed checkmark",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}


// --- TAB 2: MISSION (DAILY READING INPUT & GUESSING) ---

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MissionInputScreen(viewModel: VocabViewModel) {
    val dailyInput by viewModel.selectedDailyInput.collectAsStateWithLifecycle()
    val tappedWordItem by viewModel.tappedWord.collectAsStateWithLifecycle()
    val guessedStatus by viewModel.guessedStatus.collectAsStateWithLifecycle()
    val selectedGuessOption by viewModel.selectedGuessOption.collectAsStateWithLifecycle()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsStateWithLifecycle()

    var userOwnSentence by remember { mutableStateOf("") }
    var showSelfSentenceInput by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Difficulty Level Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Beginner", "Intermediate", "Advanced").forEach { diff ->
                val active = selectedDifficulty == diff
                Button(
                    onClick = { viewModel.updateDailyDifficulty(diff) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = if (active) 2.dp else 0.dp)
                ) {
                    Text(diff, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Daily Content Display Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Category Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = dailyInput?.topic ?: "Vocabulary Building",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = dailyInput?.title ?: "Today's Context Mission",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "By ${dailyInput?.author ?: "Coach"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Main Article Body text with clickable word tags
                Text(
                    text = "Coach Tip: Tap on any bold word to play the Guessing game and save the definitions!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))

                val customWordsList = dailyInput?.keyWords ?: emptyList()
                val fullTextParts = dailyInput?.text?.split(" ") ?: emptyList()

                // Create a row flow layout of text with searchable words
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    fullTextParts.forEach { part ->
                        val cleanPart = part.lowercase().trim().replace(Regex("[^a-zA-Z]"), "")
                        val isKey = customWordsList.any { it.lowercase() == cleanPart }
                        
                        if (isKey) {
                            Text(
                                text = part,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                                    .padding(horizontal = 4.dp)
                                    .clickable { viewModel.tapOnWordOfInput(part) }
                            )
                        } else {
                            Text(
                                text = part,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }

        // Guessing Interactive Frame (displays only when user clicks a word)
        tappedWordItem?.let { word ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Context Guessing challenge",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sentence: \"...${word.exampleSentence}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "What do you think \"${word.word}\" means from the context?",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 4 Options
                    val mockOptions = when (word.word) {
                        "flexible" -> listOf("Easy to change or adjust", "Very expensive", "Difficult to understand", "Related to local food")
                        "automation" -> listOf("Simple hand calculations", "Using computers/machines to do work automatically", "Buying assets", "Writing paper mail")
                        "scholarship" -> listOf("Financial tuition aid given to a student", "A campus science library book", "An application form", "A study desk")
                        "confident" -> listOf("Feeling sure about oneself", "Insecure and silent", "Noisy or angry", "Unaware of options")
                        "obsolete" -> listOf("Out of date or useless", "Very fast and modern", "Brightly colored", "Expensive to build")
                        "intuitive" -> listOf("Easy to understand or use without training", "Complex and technical", "Unfriendly design", "Damaged")
                        else -> listOf("Able to change easily", "Damaged/Broken", "Slow and heavy", "Dangerous")
                    }

                    mockOptions.forEach { option ->
                        val selected = selectedGuessOption == option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (selected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.03f)
                                )
                                .clickable(enabled = guessedStatus == null) {
                                    viewModel.submitContextGuess(option)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (selected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (selected) MaterialTheme.colorScheme.secondary else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(option, modifier = Modifier.weight(1f))
                        }
                    }

                    // Guess result display
                    guessedStatus?.let { correct ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (correct) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else Color.Red.copy(alpha = 0.06f)
                                )
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(
                                        imageVector = if (correct) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = if (correct) MaterialTheme.colorScheme.primary else Color.Red
                                    )
                                    Text(
                                        text = if (correct) "Correct Guess! +10 XP" else "Nice try! Learning is key.",
                                        fontWeight = FontWeight.Bold,
                                        color = if (correct) MaterialTheme.colorScheme.primary else Color.Red
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Definition: ${word.meaning}",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Explanation: ${word.explanation}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Pronunciation: [ ${word.pronunciation} ]", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Synonyms: ${word.synonyms.joinToString(", ")}", fontSize = 12.sp, color = Color.Gray)
                                Text("Collocations: ${word.collocations.joinToString(", ")}", fontSize = 12.sp, color = Color.Gray)

                                Spacer(modifier = Modifier.height(12.dp))

                                // Save Word Row
                                if (!showSelfSentenceInput) {
                                    Button(
                                        onClick = { showSelfSentenceInput = true },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Save Word to Notebook")
                                    }
                                } else {
                                    Text("Make your own sentence:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = userOwnSentence,
                                        onValueChange = { userOwnSentence = it },
                                        placeholder = { Text("e.g. I received a scholarship due to high academic performance.") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("onboard_sentence_input"),
                                        singleLine = true
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            viewModel.saveTappedWordToNotebook(userOwnSentence)
                                            showSelfSentenceInput = false
                                            userOwnSentence = ""
                                            viewModel.tappedWord.value = null // clear tapped card
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Text("Add & Schedule Review")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// --- TAB 3: NOTEBOOK (DICTIONARY) ---

@Composable
fun VocabularyNotebookScreen(viewModel: VocabViewModel) {
    val savedWords by viewModel.vocabWords.collectAsStateWithLifecycle()
    val reviewsList by viewModel.wordsForReview.collectAsStateWithLifecycle()
    
    var searchWord by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var activeDetailWord by remember { mutableStateOf<VocabularyWord?>(null) }
    
    // Add Word Dialog values
    var showAddDialog by remember { mutableStateOf(false) }
    var addWord by remember { mutableStateOf("") }
    var addWordType by remember { mutableStateOf("adjective") }
    var addMeaning by remember { mutableStateOf("") }
    var addSentence by remember { mutableStateOf("") }
    var addNative by remember { mutableStateOf("") }

    val categories = listOf("All", "Education", "Technology", "AI", "Interview", "Custom")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search & Add word row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchWord,
                onValueChange = { searchWord = it },
                placeholder = { Text("Search Notebook...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("search_notebook_field"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Word")
            }
        }

        // Category scroll bar
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val active = selectedCategoryFilter == cat
                FilterChip(
                    selected = active,
                    onClick = { selectedCategoryFilter = cat },
                    label = { Text(cat) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        // Reviews Prompt Panel
        if (reviewsList.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.Timelapse, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        "${reviewsList.size} Words scheduled for Spaced Repetition Review today!",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { activeDetailWord = reviewsList.first() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Review Now", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Filter and display words list
        val filtered = savedWords.filter { w ->
            val matchSearch = w.word.contains(searchWord, ignoreCase = true) || w.meaning.contains(searchWord, ignoreCase = true)
            val matchCatalog = if (selectedCategoryFilter == "All") true 
                               else if (selectedCategoryFilter == "Custom") w.topic == "Personal Custom"
                               else w.topic.contains(selectedCategoryFilter, ignoreCase = true) || w.topic.contains(selectedCategoryFilter, ignoreCase = true)
            matchSearch && matchCatalog
        }

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No saved words found.", color = Color.Gray, fontWeight = FontWeight.Bold)
                    Text("Go to Missions and save vocabulary!", fontSize = 12.sp, color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { w ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeDetailWord = w },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(w.word, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(w.wordType, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Text(w.meaning, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("Mastery: ${w.masteryLevel}/5", fontSize = 10.sp, color = Color.Gray)
                                    Text("Reviews: ${w.reviewCount}", fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                            
                            IconButton(onClick = { viewModel.deleteWordFromNotebook(w) }) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }
        }
    }

    // WORD DETAIL DIALOG (Spaced Repressed Active Recall review included!)
    activeDetailWord?.let { word ->
        AlertDialog(
            onDismissRequest = { activeDetailWord = null },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.performReviewWord(word, isCorrect = true)
                        activeDetailWord = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Mastered (+12 XP)")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.performReviewWord(word, isCorrect = false)
                        activeDetailWord = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Needs Practice")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(word.word.uppercase(), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(word.wordType, color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Meaning:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(word.meaning, style = MaterialTheme.typography.bodyLarge)
                    
                    if (word.nativeMeaning.isNotBlank()) {
                         Text("Native translation:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                         Text(word.nativeMeaning)
                    }

                    Text("Explanation:", fontWeight = FontWeight.Bold)
                    Text(word.simpleExplanation, fontSize = 13.sp, color = Color.Gray)

                    Text("Context Example:", fontWeight = FontWeight.Bold)
                    Text("\"${word.exampleSentence}\"", style = MaterialTheme.typography.bodyMedium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)

                    if (word.userSentence.isNotBlank()) {
                        Text("My Sentence:", fontWeight = FontWeight.Bold)
                        Text("\"${word.userSentence}\"", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                    }

                    if (word.synonyms.isNotBlank()) {
                        Text("Synonyms:", fontWeight = FontWeight.Bold)
                        Text(word.synonyms.split(";").joinToString(", "))
                    }
                    if (word.collocations.isNotBlank()) {
                        Text("Useful Collocations:", fontWeight = FontWeight.Bold)
                        Text(word.collocations.split(";").joinToString(", "), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("Did you remember this word context correctly?", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // MANUAL ADD WORD DIALOG
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        if (addWord.isNotBlank() && addMeaning.isNotBlank()) {
                            viewModel.addWordManually(addWord, addWordType, addMeaning, addSentence, addNative)
                            addWord = ""
                            addMeaning = ""
                            addSentence = ""
                            addNative = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Add Word")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            },
            title = { Text("Add Word Manually", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(value = addWord, onValueChange = { addWord = it }, label = { Text("Word (English)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    
                    // Word Type Selector
                    Column {
                        Text("Word Type", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("noun", "verb", "adjective", "adverb", "phrase").forEach { type ->
                                val sel = addWordType == type
                                FilterChip(
                                    selected = sel,
                                    onClick = { addWordType = type },
                                    label = { Text(type, fontSize = 10.sp) }
                                )
                            }
                        }
                    }

                    OutlinedTextField(value = addMeaning, onValueChange = { addMeaning = it }, label = { Text("Meaning") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = addNative, onValueChange = { addNative = it }, label = { Text("Native Meaning (Urdu/Other) optional") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = addSentence, onValueChange = { addSentence = it }, label = { Text("Example Sentence") }, modifier = Modifier.fillMaxWidth())
                }
            }
        )
    }
}


// --- TAB 4: GAMES HUBS (GAME SANDBOX) ---

@Composable
fun GamesScreen(viewModel: VocabViewModel) {
    var activeGameId by remember { mutableStateOf(0) } // 0: hub, 1: fill blanks, 2: synonym matcher, 3: "Very" upgrade, 4: sentence builder, 5: story word generator, 6: AI Notebook Quiz

    when (activeGameId) {
        0 -> GameSelectionHub(onSelectGame = { id -> activeGameId = id })
        1 -> FillBlanksGameView(viewModel, onBack = { activeGameId = 0 })
        2 -> SynonymMatcherGameView(viewModel, onBack = { activeGameId = 0 })
        3 -> VeryUpgradeGameView(viewModel, onBack = { activeGameId = 0 })
        4 -> SentenceBuilderGameView(viewModel, onBack = { activeGameId = 0 })
        5 -> WordStoryGameView(viewModel, onBack = { activeGameId = 0 })
        6 -> AiNotebookQuizGameView(viewModel, onBack = { activeGameId = 0 })
    }
}

// GAMES SELECTION INDEX HUB
@Composable
fun GameSelectionHub(onSelectGame: (Int) -> Unit) {
    val options = listOf(
        Triple(1, "Fill in the Blanks", "Timed context verification with instant scoring and explanations."),
        Triple(2, "Synonym Matcher", "Upgraded pairing of simple dictionary words with better vocabulary."),
        Triple(3, "Don't Say 'Very'", "Practice replacing clumsy phrases with powerful Material adjectives."),
        Triple(4, "Sentence Builder", "Reassemble scrambled high-priority sentences in proper syntax tenses."),
        Triple(5, "Word Story AI Game", "AI-evaluated fun stories written using saved notebook key terms."),
        Triple(6, "AI Notebook Quiz (Dynamic)", "Generates a customized challenge quiz directly from your saved notebook vocabulary using Gemini.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Quest Learning Sandbox",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Boost active retention through highly engaging English games!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))

        options.forEach { (id, title, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectGame(id) }
                    .testTag("game_card_#${id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (id == 6) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (id) {
                            1 -> Icons.Default.Timer
                            2 -> Icons.Default.SwapHoriz
                            3 -> Icons.Default.Upgrade
                            4 -> Icons.Default.FormatAlignLeft
                            5 -> Icons.Default.AutoAwesome
                            else -> Icons.Default.FlashOn
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (id == 6) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                            if (id == 6) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("AI", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.tertiary)
                                }
                            }
                        }
                        Text(desc, fontSize = 12.sp, color = Color.Gray)
                    }

                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}


// GAME 1: FILL IN BLANKS
@Composable
fun FillBlanksGameView(viewModel: VocabViewModel, onBack: () -> Unit) {
    val index by viewModel.fillBlanksIndex.collectAsStateWithLifecycle()
    val score by viewModel.fillBlanksScore.collectAsStateWithLifecycle()
    val answersList by viewModel.fillBlanksAnswers.collectAsStateWithLifecycle()
    val showResult by viewModel.fillBlanksShowResult.collectAsStateWithLifecycle()

    val qList by viewModel.dynamicFillBlanksQuestions.collectAsStateWithLifecycle()
    val currentQ = qList.getOrNull(index)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Notebook Blank Quest ✍️", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
            Text("Score: $score", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        if (showResult) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(Icons.Default.Stars, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                    Text("Game Completed!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Your Final Score: $score / 50 XP", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.secondary)
                    
                    Text("Summary: ${answersList.count { it }} Correct, ${answersList.count { !it }} Mistakes")
                    
                    Button(
                        onClick = { viewModel.restartFillBlanks() },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Play Again")
                    }
                }
            }
        } else {
            currentQ?.let { q ->
                // Progress indicator bar
                val totalQs = if (qList.isNotEmpty()) qList.size else 5
                LinearProgressIndicator(
                    progress = (index.toFloat() / totalQs.toFloat()).coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Text("Question ${index + 1} of $totalQs", color = Color.Gray, fontSize = 12.sp)

                // Flash Card styling in question area with Interactive 3D Flip
                FlashCardView(
                    frontContent = {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Sentence Challenge",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = q.sentenceWithBlank,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "👆 Tap Card to Reveal Hint Clues",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    },
                    backContent = {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Vocabulary Tip Clues 💡",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Syntactically matches native sentence structures from studied notebook words. Check spelling structures of options below closely!",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "👆 Tap Card to return to sentence",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                )

                Text("Choose the correct word:", fontWeight = FontWeight.Bold)

                q.options.forEachIndexed { optIndex, option ->
                    Button(
                        onClick = { viewModel.submitFillBlankAnswer(optIndex) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), contentColor = MaterialTheme.colorScheme.onBackground)
                    ) {
                        Text(option, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}


// GAME 2: SYNONYM MATCHER
@Composable
fun SynonymMatcherGameView(viewModel: VocabViewModel, onBack: () -> Unit) {
    val matches by viewModel.synonymMatches.collectAsStateWithLifecycle()
    val selectedWeak by viewModel.selectedSynonymWeak.collectAsStateWithLifecycle()
    val score by viewModel.synonymScore.collectAsStateWithLifecycle()
    val completed by viewModel.synonymMatchingCompleted.collectAsStateWithLifecycle()

    val wordPairs by viewModel.dynamicSynonymPairs.collectAsStateWithLifecycle()
    val activePairs = wordPairs.take(4)

    val remainingWeaks = remember(activePairs, matches) {
        activePairs.map { it.weak }.filter { !matches.containsKey(it) }
    }
    val remainingStrongs = remember(activePairs, matches) {
        activePairs.map { it.strong }.filter { !matches.containsValue(it) }.shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Dynamic Synonym Connect", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
            Text("Score: $score", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        if (completed) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(Icons.Default.Celebration, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                    Text("Perfect Match!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Final Score: $score XP", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.secondary)
                    
                    Button(
                        onClick = { viewModel.restartSynonymMatching() },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Connect More")
                    }
                }
            }
        } else {
            Text("Connect standard simple words (left) to advanced synonyms (right) compiled dynamically from studied notebook items:", color = Color.Gray, fontSize = 12.sp)

            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                // Weak column (Left)
                Column(modifier = Modifier.weight(1f).padding(4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Simple", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                    remainingWeaks.forEach { w ->
                        val active = selectedWeak == w
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectSynonymMatchLeft(w) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (active) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(2.dp, if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                        ) {
                            Text(w, modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1)
                        }
                    }
                }

                // Strong column (Right)
                Column(modifier = Modifier.weight(1f).padding(4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Advanced Upgrades", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)
                    remainingStrongs.forEach { s ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.connectSynonymMatchRight(s) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                        ) {
                            Text(s, modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1)
                        }
                    }
                }
            }

            // Matches Completed list
            if (matches.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f))) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Matched connections:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        matches.forEach { (k, v) ->
                            Text("✓ $k  ➔  $v", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}


// GAME 3: DON'T SAY "VERY"
@Composable
fun VeryUpgradeGameView(viewModel: VocabViewModel, onBack: () -> Unit) {
    val index by viewModel.veryIndex.collectAsStateWithLifecycle()
    val score by viewModel.veryScore.collectAsStateWithLifecycle()
    val selectedOption by viewModel.verySelectedOption.collectAsStateWithLifecycle()
    val showResult by viewModel.veryShowResult.collectAsStateWithLifecycle()

    val currentQ = MockData.dontSayVery.getOrNull(index)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Don't Say 'Very'", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
            Text("Score: $score", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        if (showResult) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(Icons.Default.ThumbUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                    Text("Splendid Upgrade Work!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Total Game Score: $score XP", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.secondary)
                    
                    Button(
                        onClick = { viewModel.restartDonationVery() },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Upgrade More Phrases")
                    }
                }
            }
        } else {
            currentQ?.let { q ->
                Text("Phrase Upgrade ${index + 1} of ${MockData.dontSayVery.size}", color = Color.Gray, fontSize = 12.sp)

                Text("How do you upgrade this weak descriptive phrase?", fontWeight = FontWeight.Bold)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = "very ${q.weak}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }

                val options = when (q.weak) {
                    "good" -> listOf("excellent", "bad", "standard")
                    "important" -> listOf("simple", "essential", "obsolete")
                    "useful" -> listOf("harmful", "valuable / practical", "broken")
                    "hard" -> listOf("intuitive", "challenging", "timid")
                    "easy" -> listOf("simple", "rigid", "convoluted")
                    "sure" -> listOf("timid", "confident", "obsolete")
                    else -> listOf(q.strong, "weak Option", "neutral Option")
                }.shuffled(java.util.Random(q.hashCode().toLong()))

                options.forEach { opt ->
                    Button(
                        onClick = { viewModel.selectVeryOption(opt) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))
                    ) {
                        Text(opt, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}


// GAME 4: SCRAMBLED SENTENCE BUILDER
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderGameView(viewModel: VocabViewModel, onBack: () -> Unit) {
    val index by viewModel.scrambledIndex.collectAsStateWithLifecycle()
    val score by viewModel.scrambledScore.collectAsStateWithLifecycle()
    val selectedWords by viewModel.scrambledSelectedWords.collectAsStateWithLifecycle()
    val completed by viewModel.scrambledCompleted.collectAsStateWithLifecycle()

    val currentQ = MockData.scrambledSentences.getOrNull(index)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Sentence Builder", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
            Text("Score: $score", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        if (completed) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(Icons.Default.FactCheck, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                    Text("Perfect Syntax!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Total Game Score: $score XP", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.secondary)
                    
                    Button(
                        onClick = { viewModel.restartScrambled() },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Rebuild Sentences")
                    }
                }
            }
        } else {
            currentQ?.let { q ->
                Text("Sentence ${index + 1} of ${MockData.scrambledSentences.size}", color = Color.Gray, fontSize = 12.sp)
                
                // Display Hint
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f))) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Text(q.hint, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                    }
                }

                // Selected Output Area
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            selectedWords.forEach { word ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(word, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { viewModel.popScrambledWord() }) {
                                Icon(Icons.Default.Undo, contentDescription = "Undo")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delete Last")
                            }
                        }
                    }
                }

                Text("Tap blocks in correct sequence:", fontWeight = FontWeight.Bold)

                // Words Choice Bank
                val selectionBank = q.scrambled.filter { !selectedWords.contains(it) }
                FlowRow(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectionBank.forEach { word ->
                        Card(
                            modifier = Modifier
                                .clickable { viewModel.selectScrambledWord(word) },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        ) {
                            Text(word, modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


// GAME 5: WORD STORY GAME
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordStoryGameView(viewModel: VocabViewModel, onBack: () -> Unit) {
    val savedWords by viewModel.vocabWords.collectAsStateWithLifecycle()
    val selectedWords by viewModel.selectedStoryWords.collectAsStateWithLifecycle()
    val storyInput by viewModel.storyTextInput.collectAsStateWithLifecycle()
    val storyFeedback by viewModel.storyResultFeedback.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Word Story AI Game", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
        }

        Text("Select 3-5 vocabulary words from your notebook to include in your funny story:", color = Color.Gray)

        if (savedWords.isEmpty()) {
            Text("You first need to save some vocabulary words in your notebook to write stories!", color = Color.Red, fontWeight = FontWeight.Bold)
        } else {
            // Words Scroll list
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                savedWords.forEach { word ->
                    val selected = selectedWords.contains(word.word)
                    FilterChip(
                        selected = selected,
                        onClick = { viewModel.selectStoryWordToggle(word.word) },
                        label = { Text(word.word, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Text input area
            OutlinedTextField(
                value = storyInput,
                onValueChange = { viewModel.storyTextInput.value = it },
                label = { Text("Write your short funny story here...") },
                placeholder = { Text("Use all selected keywords in correct, natural context sentences.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("story_input_field"),
                shape = RoundedCornerShape(12.dp)
            )

            // Submit row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.submitWordStoryForEvaluation() },
                    modifier = Modifier.weight(1f),
                    enabled = !isAnalyzing && selectedWords.isNotEmpty() && storyInput.isNotBlank()
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Coach evaluation Story")
                    }
                }
                OutlinedButton(onClick = { viewModel.clearWordStory() }) {
                    Text("Clear")
                }
            }

            // Evaluation Box
            storyFeedback?.let { feedback ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "AI Coaching Analysis",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(feedback, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}


// --- TAB 5: PREPARATION (ADMISSIONS COACH) ---

@Composable
fun InterviewCoachScreen(viewModel: VocabViewModel) {
    var sectionId by remember { mutableStateOf(1) } // 1: USA Interview Mock, 2: Writing, 3: AI Coach Direct Chat

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Toggle bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(Triple(1, "Mock Interview", Icons.Default.VolumeUp), Triple(2, "Writing Lab", Icons.Default.Edit), Triple(3, "Direct AI Coach", Icons.Default.ChatBubble)).forEach { (id, label, icon) ->
                val active = sectionId == id
                Button(
                    onClick = { sectionId = id },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                    ),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))

        // Toggle render sections
        when (sectionId) {
            1 -> MockInterviewSectionView(viewModel)
            2 -> WritingLabSectionView(viewModel)
            3 -> DirectCoachChatSectionView(viewModel)
        }
    }
}


// SEC 1: USA MOCK INTERVIEW
@Composable
fun MockInterviewSectionView(viewModel: VocabViewModel) {
    val activeQuestion by viewModel.activeInterviewQuestion.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecordingMock.collectAsStateWithLifecycle()
    val textTranscript by viewModel.currentSpeechTranscript.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isSpeechAnalysisLoading.collectAsStateWithLifecycle()
    val evalResult by viewModel.speechEvaluationResult.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (activeQuestion == null) {
            item {
                Text("Select a critical USA visa/admission interview topic and practice speaking:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(MockData.interviewQuestions) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.activeInterviewQuestion.value = item },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.topic, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text(item.question, fontSize = 13.sp, color = Color.Gray)
                        }
                        Icon(Icons.Default.Mic, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        } else {
            val qInfo = activeQuestion!!
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { viewModel.activeInterviewQuestion.value = null }) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                    Text(qInfo.topic, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Question:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(qInfo.question, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Expected vocabulary: ${qInfo.keyVocabulary.joinToString(", ")}", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Answer Templates Cards
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Standard sample templates (Click to expand)", fontWeight = FontWeight.Bold)
                        
                        Text("Weak / Simplistic Answer:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.Red.copy(alpha = 0.7f))
                        Text("\"${qInfo.simpleSample}\"", fontSize = 13.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)

                        Divider()

                        Text("Natural / IELTS answer:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        Text("\"${qInfo.naturalSample}\"", fontSize = 13.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)

                        Divider()

                        Text("Admissions Advanced level:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        Text("\"${qInfo.advancedSample}\"", fontSize = 13.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Recorder Control visual Area
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("VocabQuest Smart audio recorder", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                        if (isRecording) {
                            Text("● RECORDING IN PROGRESS...", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            // simulated ripple sound visualizer bar
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                repeat(8) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 4.dp, height = (20..50).random().dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                            Button(
                                onClick = { viewModel.stopRecordingSpeechAndTranscribe(qInfo.question) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Stop & Transcribe Response")
                            }
                        } else {
                            Text("Ready to verify your pronunciation and fluency?", color = Color.Gray, fontSize = 12.sp)
                            Button(
                                onClick = { viewModel.startRecordingSpeech() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Start Fluent Recording")
                            }
                        }

                        if (isAnalyzing) {
                            Spacer(modifier = Modifier.height(8.dp))
                            CircularProgressIndicator()
                            Text("AI admissions coach is analyzing grammar stability...", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }

                // AI evaluation Results
                evalResult?.let { res ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("AI Coaching feedback sheet", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Fluency" to res.fluencyScore, "Grammar" to res.grammarScore, "Vocab" to res.vocabScore, "Confidence" to res.confidenceScore).forEach { (scoreLabel, scoreVal) ->
                                    Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))) {
                                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(scoreLabel, fontSize = 10.sp, color = Color.Gray)
                                            Text("$scoreVal", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                }
                            }

                            Divider()

                            Text("Your Speech Transcript:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("\"${textTranscript}\"", fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)

                            Divider()

                            Text("Grammar & Filler Word Correction Card:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(res.feedbackMarkdown)

                            Divider()

                            Text("Upgraded natural response sample:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                            Text(res.naturalText)

                            Text("Scholarship advanced admissions standard:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            Text(res.advancedText)
                        }
                    }
                }
            }
        }
    }
}


// SEC 2: WRITING LAB
@Composable
fun WritingLabSectionView(viewModel: VocabViewModel) {
    val currentPrompt by viewModel.currentWritingPrompt.collectAsStateWithLifecycle()
    val writingInput by viewModel.writingInputText.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isWritingAnalysisLoading.collectAsStateWithLifecycle()
    val evalResult by viewModel.writingEvaluationResult.collectAsStateWithLifecycle()

    val presetPromptsList = listOf(
        "Why is database security essential in modern AI development?",
        "Why do you want to pursue higher graduate coursework in computer science?",
        "What are the ethical challenges with automation replacing local manual workers?"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Select a study writing essay prompt to evaluate writing grammar structure:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(presetPromptsList) { p ->
            val sel = currentPrompt == p
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectWritingPrompt(p) },
                colors = CardDefaults.cardColors(
                    containerColor = if (sel) MaterialTheme.colorScheme.primary.copy(alpha = 0.07f) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, if (sel) MaterialTheme.colorScheme.primary else Color.Transparent)
            ) {
                Text(p, modifier = Modifier.padding(14.dp), fontSize = 13.sp, fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = writingInput,
                onValueChange = { viewModel.writingInputText.value = it },
                label = { Text("Write your paragraph essay contribution here...") },
                placeholder = { Text("Formulate 3-5 sentences that describe your knowledge on this essay topic.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("writing_input_box"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.submitWritingAnswer() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isAnalyzing && writingInput.isNotBlank()
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Evaluate Essay Output (+25 XP)")
                }
            }
        }

        evalResult?.let { res ->
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("IELTS Essay Corrective Card", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                        
                        Text("Parsed Corrections:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(res.correctedText)

                        Divider()

                        Text("Style Upgrades & Spelling checks:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(res.feedbackMarkdown)

                        Divider()

                        Text("Naturally fluent version:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        Text(res.naturalText)

                        Text("Scholarly advanced version:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        Text(res.advancedText)
                    }
                }
            }
        }
    }
}


// SEC 3: DIRECT COACH CHAT
@Composable
fun DirectCoachChatSectionView(viewModel: VocabViewModel) {
    val messages by viewModel.aiCoachMessages.collectAsStateWithLifecycle()
    val input by viewModel.currentCoachInput.collectAsStateWithLifecycle()
    val isResponding by viewModel.isCoachResponding.collectAsStateWithLifecycle()
    
    val listState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(listState)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(4.dp)) {
                messages.forEach { msg ->
                    val isCoach = msg.sender == "coach"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isCoach) Arrangement.Start else Arrangement.End
                    ) {
                        Card(
                            modifier = Modifier.widthIn(max = 280.dp),
                            shape = RoundedCornerShape(
                                topStart = 16.dp, 
                                topEnd = 16.dp, 
                                bottomStart = if (isCoach) 0.dp else 16.dp, 
                                bottomEnd = if (isCoach) 16.dp else 0.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCoach) MaterialTheme.colorScheme.surface 
                                                else MaterialTheme.colorScheme.primary
                            ),
                            border = if (isCoach) BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)) else null
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(
                                        imageVector = if (isCoach) Icons.Default.Face else Icons.Default.Person,
                                        contentDescription = null,
                                        tint = if (isCoach) MaterialTheme.colorScheme.primary else Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isCoach) "AI English Coach" else "You",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = if (isCoach) MaterialTheme.colorScheme.primary else Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = msg.text,
                                    color = if (isCoach) MaterialTheme.colorScheme.onSurface else Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
                
                if (isResponding) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp))
                            Text("Coach is thinking...", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input send field row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { viewModel.currentCoachInput.value = it },
                placeholder = { Text("Ask Coach grammar or speak plans...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("coach_chat_text_box"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            IconButton(
                onClick = {
                    viewModel.sendCoachMessage()
                    scope.launch {
                        delay(100)
                        listState.animateScrollTo(listState.maxValue)
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .size(48.dp),
                enabled = !isResponding && input.isNotBlank()
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Composable
fun AiNotebookQuizGameView(viewModel: VocabViewModel, onBack: () -> Unit) {
    val questions by viewModel.aiQuizQuestions.collectAsStateWithLifecycle()
    val currentIndex by viewModel.aiQuizCurrentIndex.collectAsStateWithLifecycle()
    val score by viewModel.aiQuizScore.collectAsStateWithLifecycle()
    val answersList by viewModel.aiQuizUserAnswers.collectAsStateWithLifecycle()
    val showResult by viewModel.aiQuizCompleted.collectAsStateWithLifecycle()
    val isLoading by viewModel.isAiQuizLoading.collectAsStateWithLifecycle()
    val notebookWords by viewModel.vocabWords.collectAsStateWithLifecycle()
    val feedbackMsg by viewModel.aiQuizFeedbackMessage.collectAsStateWithLifecycle()
    val selectedOption by viewModel.aiQuizSelectedOption.collectAsStateWithLifecycle()

    val currentQ = questions.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("AI Notebook Quiz", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    "Score: $score",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        strokeWidth = 5.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Gemini AI is analyzing...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Curating custom grammar questions based on your recorded notebook vocabulary. This will compile dynamic blanks...",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(max = 280.dp)
                    )
                }
            }
        } else if (questions.isEmpty()) {
            // Intro Dashboard Screen before generating
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        "AI Dynamic Notebook Quiz",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        "This game dynamically scans all words you've learned & saved inside your personal notebook. Gemini then builds challenging context sentences to test your active recall!",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 20.sp
                    )

                    // Display info about active words count
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = "Notebook status: ${notebookWords.size} words saved",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = if (notebookWords.isEmpty()) "We will use high-yield standard terms instead!" 
                                           else "AI will construct specialized questions testing these terms.",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { viewModel.generateAiQuizFromNotebook() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_generate_ai_quiz"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.FlashOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate AI Custom Quiz", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        } else if (showResult) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD54F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Text(
                        text = "Quest Quiz Completed!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Nice practice! Dynamic retention active recall is the fastest way to program native fluency parameters.",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("XP EARNED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Text("+$score XP", fontSize = 24.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("ACCURACY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            val correct = answersList.count { it }
                            Text("$correct / ${questions.size}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Close Suite")
                        }
                        Button(
                            onClick = { viewModel.restartAiQuiz() },
                            modifier = Modifier.weight(1.5f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Play Again")
                        }
                    }
                }
            }
        } else {
            currentQ?.let { q ->
                // Banner showing AI status
                feedbackMsg?.let { msg ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = msg,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Question progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${currentIndex + 1} of ${questions.size}",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Progress: ${((currentIndex + 1).toFloat() / questions.size * 100).toInt()}%",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                LinearProgressIndicator(
                    progress = ((currentIndex + 1).toFloat() / questions.size),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // The Question Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Fill in the Blank context:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = q.sentenceWithBlank,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Options layout
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    q.options.forEachIndexed { i, opt ->
                        val hasAnswered = selectedOption != null
                        val isCorrectOption = i == q.correctIndex
                        val isSelected = selectedOption == opt

                        val containerColor = if (hasAnswered) {
                            if (isCorrectOption) Color(0xFFD4EDDA) // correct green
                            else if (isSelected) Color(0xFFF8D7DA) // wrong red
                            else MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.surface
                        }

                        val contentColor = if (hasAnswered) {
                            if (isCorrectOption) Color(0xFF155724)
                            else if (isSelected) Color(0xFF721C24)
                            else MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }

                        val borderColor = if (hasAnswered) {
                            if (isCorrectOption) Color(0xFFC3E6CB)
                            else if (isSelected) Color(0xFFF5C6CB)
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 52.dp)
                                .clickable(enabled = !hasAnswered) {
                                    viewModel.submitAiQuizAnswer(i)
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = containerColor),
                            border = BorderStroke(1.5.dp, borderColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(contentColor.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = (65 + i).toChar().toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = contentColor
                                    )
                                }

                                Text(
                                    text = opt,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = contentColor,
                                    modifier = Modifier.weight(1f)
                                )

                                if (hasAnswered) {
                                    if (isCorrectOption) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF28A745))
                                    } else if (isSelected) {
                                        Icon(Icons.Default.Cancel, contentDescription = "Wrong", tint = Color(0xFFDC3545))
                                    }
                                }
                            }
                        }
                    }
                }

                // AI Contextual explanation card slides up.
                if (selectedOption != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    "AI Explanation:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = q.explanation,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
