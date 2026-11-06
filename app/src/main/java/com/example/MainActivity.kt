package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Recipe
import com.example.ui.screens.AddRecipeScreen
import com.example.ui.screens.CookingModeScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.MealPlannerScreen
import com.example.ui.screens.RecipeDetailScreen
import com.example.ui.screens.ShoppingListScreen
import com.example.ui.theme.FlavorCraftTheme
import com.example.ui.viewmodel.RecipeViewModel

enum class MainDestination(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DISCOVER("Cookbook", Icons.Filled.RestaurantMenu, Icons.Outlined.RestaurantMenu, "nav_cookbook"),
    MEAL_PLANNER("Meal Plan", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth, "nav_meal_plan"),
    SHOPPING_LIST("Groceries", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart, "nav_groceries")
}

enum class SubScreen {
    NONE,
    RECIPE_DETAIL,
    COOKING_MODE,
    ADD_RECIPE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlavorCraftTheme {
                FlavorCraftApp()
            }
        }
    }
}

@Composable
fun FlavorCraftApp(viewModel: RecipeViewModel = viewModel()) {
    var currentDestination by rememberSaveable { mutableStateOf(MainDestination.DISCOVER) }
    var currentSubScreen by rememberSaveable { mutableStateOf(SubScreen.NONE) }

    val selectedRecipe by viewModel.selectedRecipe.collectAsStateWithLifecycle()
    val shoppingItems by viewModel.shoppingItems.collectAsStateWithLifecycle()
    val pendingShoppingCount = shoppingItems.count { !it.isCompleted }

    val snackbarHostState = remember { SnackbarHostState() }

    // Listen to user feedback messages from ViewModel
    LaunchedEffect(viewModel) {
        viewModel.userMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Global back handling
    BackHandler(enabled = currentSubScreen != SubScreen.NONE || currentDestination != MainDestination.DISCOVER) {
        when {
            currentSubScreen == SubScreen.COOKING_MODE -> {
                viewModel.exitCookingMode()
                currentSubScreen = SubScreen.RECIPE_DETAIL
            }
            currentSubScreen != SubScreen.NONE -> {
                currentSubScreen = SubScreen.NONE
                viewModel.clearSelectedRecipe()
            }
            currentDestination != MainDestination.DISCOVER -> {
                currentDestination = MainDestination.DISCOVER
            }
        }
    }

    val isFullscreenSubScreen = currentSubScreen in listOf(
        SubScreen.RECIPE_DETAIL,
        SubScreen.COOKING_MODE,
        SubScreen.ADD_RECIPE
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AnimatedVisibility(
                visible = !isFullscreenSubScreen,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("main_bottom_nav"),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    MainDestination.entries.forEach { destination ->
                        val selected = currentDestination == destination
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                currentDestination = destination
                                currentSubScreen = SubScreen.NONE
                            },
                            icon = {
                                if (destination == MainDestination.SHOPPING_LIST && pendingShoppingCount > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            ) {
                                                Text(text = "$pendingShoppingCount")
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                            contentDescription = destination.title,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                        contentDescription = destination.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = destination.title,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag(destination.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isFullscreenSubScreen) androidx.compose.foundation.layout.PaddingValues(0.dp) else innerPadding)
        ) {
            when (currentSubScreen) {
                SubScreen.COOKING_MODE -> {
                    CookingModeScreen(
                        viewModel = viewModel,
                        onExitCookingMode = {
                            currentSubScreen = SubScreen.RECIPE_DETAIL
                        }
                    )
                }

                SubScreen.RECIPE_DETAIL -> {
                    selectedRecipe?.let { recipe ->
                        RecipeDetailScreen(
                            recipe = recipe,
                            viewModel = viewModel,
                            onBackClick = {
                                currentSubScreen = SubScreen.NONE
                                viewModel.clearSelectedRecipe()
                            },
                            onStartCooking = { r ->
                                viewModel.startCookingMode(r)
                                currentSubScreen = SubScreen.COOKING_MODE
                            }
                        )
                    } ?: run {
                        currentSubScreen = SubScreen.NONE
                    }
                }

                SubScreen.ADD_RECIPE -> {
                    AddRecipeScreen(
                        viewModel = viewModel,
                        onBackClick = { currentSubScreen = SubScreen.NONE }
                    )
                }

                SubScreen.NONE -> {
                    when (currentDestination) {
                        MainDestination.DISCOVER -> {
                            DiscoverScreen(
                                viewModel = viewModel,
                                onRecipeClick = { recipe ->
                                    viewModel.selectRecipe(recipe)
                                    currentSubScreen = SubScreen.RECIPE_DETAIL
                                },
                                onAddRecipeClick = {
                                    currentSubScreen = SubScreen.ADD_RECIPE
                                }
                            )
                        }

                        MainDestination.MEAL_PLANNER -> {
                            MealPlannerScreen(
                                viewModel = viewModel,
                                onRecipeSelect = { recipe ->
                                    viewModel.selectRecipe(recipe)
                                    currentSubScreen = SubScreen.RECIPE_DETAIL
                                }
                            )
                        }

                        MainDestination.SHOPPING_LIST -> {
                            ShoppingListScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
