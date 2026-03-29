package com.example.waterme.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflowg
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waterme.*
import com.example.waterme.data.DataSource
import com.example.waterme.data.Reminder
import com.example.waterme.model.Plant
import com.example.waterme.ui.theme.WaterMeTheme
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

// Color palette
val WaterMeGreen = Color(0xFF2E7D32)
val WaterMeLightGreen = Color(0xFF4CAF50)
val WaterMeDarkGreen = Color(0xFF1B5E20)
val WaterMeBlue = Color(0xFF0288D1)
val WaterMeLightBlue = Color(0xFF03A9F4)
val WaterMeYellow = Color(0xFFFFC107)
val WaterMeOrange = Color(0xFFFF9800)
val GradientStart = Color(0xFFE8F5E9)
val GradientEnd = Color(0xFFC8E6C9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterMeApp(waterViewModel: WaterViewModel = viewModel(factory = WaterViewModel.Factory)) {
    val layoutDirection = LocalLayoutDirection.current
    var showWelcome by remember { mutableStateOf(true) }

    WaterMeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (showWelcome) {
                AnimatedVisibility(
                    visible = showWelcome,
                    enter = fadeIn(animationSpec = tween(1000)) + scaleIn(initialScale = 0.8f),
                    exit = fadeOut(animationSpec = tween(500))
                ) {
                    WelcomeScreen(onDismiss = { showWelcome = false })
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(GradientStart, GradientEnd)
                            )
                        )
                ) {
                    PlantListContent(
                        plants = waterViewModel.plants,
                        onScheduleReminder = { waterViewModel.scheduleReminder(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(onDismiss: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(WaterMeLightGreen, WaterMeGreen),
                    center = Offset(0.5f, 0.5f),
                    radius = 1.5f
                )
            )
            .clickable { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Water Me",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Never forget to water your plants again",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = WaterMeGreen,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun PlantListContent(
    plants: List<Plant>,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPlant by rememberSaveable { mutableStateOf(plants[0]) }
    var showReminderDialog by rememberSaveable { mutableStateOf(false) }
    var expandedPlant by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            HeaderSection()
        }

        itemsIndexed(items = plants) { index, plant ->
            AnimatedItem(
                index = index,
                isExpanded = expandedPlant == plant.name.toString()
            ) {
                PlantListItem(
                    plant = plant,
                    isExpanded = expandedPlant == plant.name.toString(),
                    onItemSelect = {
                        selectedPlant = plant
                        showReminderDialog = true
                    },
                    onExpandToggle = {
                        expandedPlant = if (expandedPlant == plant.name.toString()) null
                        else plant.name.toString()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showReminderDialog) {
        ReminderDialogContent(
            onDialogDismiss = { showReminderDialog = false },
            plantName = stringResource(selectedPlant.name),
            onScheduleReminder = onScheduleReminder
        )
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = "🌱 My Garden",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = WaterMeDarkGreen
        )
        Text(
            text = "Take care of your green friends",
            fontSize = 16.sp,
            color = WaterMeGreen,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantListItem(
    plant: Plant,
    isExpanded: Boolean,
    onItemSelect: (Plant) -> Unit,
    onExpandToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = if (isExpanded) 12.dp else 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0xFF1E88E5),
                ambientColor = Color(0xFF1E88E5)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandToggle() }
        ) {
            // Header with plant icon and name
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(WaterMeLightGreen, WaterMeGreen)
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Text(
                            text = stringResource(plant.name),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            // Plant details
            AnimatedContent(
                targetState = isExpanded,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) +
                            slideInVertically(initialOffsetY = { it / 2 }) with
                            fadeOut(animationSpec = tween(300)) +
                            slideOutVertically(targetOffsetY = { it / 2 })
                }
            ) { expanded ->
                if (expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Plant type badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WaterMeLightGreen.copy(alpha = 0.1f),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = stringResource(plant.type),
                                fontSize = 14.sp,
                                color = WaterMeGreen,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        // Description
                        Text(
                            text = stringResource(plant.description),
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 20.sp
                        )

                        Divider(color = Color.LightGray.copy(alpha = 0.3f))

                        // Water schedule
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = WaterMeBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Water every:",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = stringResource(plant.schedule),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = WaterMeBlue
                            )
                        }

                        // Water button
                        Button(
                            onClick = { onItemSelect(plant) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WaterMeGreen
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Set Reminder")
                        }
                    }
                } else {
                    // Collapsed view - show summary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = stringResource(plant.type),
                                fontSize = 12.sp,
                                color = WaterMeGreen
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = WaterMeBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(plant.schedule),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = WaterMeOrange
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedItem(
    index: Int,
    isExpanded: Boolean,
    content: @Composable () -> Unit
) {
    val transition = updateTransition(
        targetState = isExpanded,
        label = "item_transition"
    )

    val scale by transition.animateFloat(
        transitionSpec = { spring(dampingRatio = 0.6f, stiffness = 300f) },
        label = "scale"
    ) { expanded ->
        if (expanded) 1.02f else 1f
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
            }
            .animateContentSize()
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDialogContent(
    onDialogDismiss: () -> Unit,
    plantName: String,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    val reminders = listOf(
        Reminder(R.string.five_seconds, FIVE_SECONDS, TimeUnit.SECONDS, plantName),
        Reminder(R.string.one_day, ONE_DAY, TimeUnit.DAYS, plantName),
        Reminder(R.string.one_week, SEVEN_DAYS, TimeUnit.DAYS, plantName),
        Reminder(R.string.one_month, THIRTY_DAYS, TimeUnit.DAYS, plantName)
    )

    AlertDialog(
        onDismissRequest = { onDialogDismiss() },
        confirmButton = {},
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = WaterMeGreen
                )
                Text(
                    text = "Remind me to water $plantName",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Choose how often you want to be reminded:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                reminders.forEach { reminder ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onScheduleReminder(reminder)
                                onDialogDismiss()
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = WaterMeLightGreen.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = when (reminder.durationRes) {
                                        R.string.five_seconds -> Icons.Default.Timer
                                        R.string.one_day -> Icons.Default.Today
                                        R.string.one_week -> Icons.Default.CalendarToday
                                        else -> Icons.Default.DateRange
                                    },
                                    contentDescription = null,
                                    tint = WaterMeGreen
                                )
                                Text(
                                    text = stringResource(reminder.durationRes),
                                    fontSize = 16.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = WaterMeGreen
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlantListItemPreview() {
    WaterMeTheme {
        PlantListItem(
            plant = DataSource.plants[0],
            isExpanded = true,
            onItemSelect = {},
            onExpandToggle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlantListContentPreview() {
    WaterMeTheme {
        PlantListContent(plants = DataSource.plants, onScheduleReminder = {})
    }
}