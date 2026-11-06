package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.IngredientItem
import com.example.data.model.InstructionStep
import com.example.data.model.MealPlanItem
import com.example.data.model.Recipe
import com.example.data.model.ShoppingItem
import com.example.data.repository.RecipeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CookingState(
    val recipe: Recipe? = null,
    val currentStepIndex: Int = 0,
    val timerSecondsRemaining: Int = 0,
    val timerTotalSeconds: Int = 0,
    val isTimerRunning: Boolean = false,
    val isTimerCompleted: Boolean = false
) {
    val totalSteps: Int get() = recipe?.instructions?.size ?: 0
    val currentStep: InstructionStep? get() = recipe?.instructions?.getOrNull(currentStepIndex)
    val progress: Float get() = if (totalSteps > 0) (currentStepIndex + 1).toFloat() / totalSteps else 0f
}

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = RecipeRepository(db.recipeDao(), db.shoppingDao(), db.mealPlanDao())
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    // Search and Category filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    // Navigation and Detail State
    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe = _selectedRecipe.asStateFlow()

    private val _servingsCount = MutableStateFlow(2)
    val servingsCount = _servingsCount.asStateFlow()

    // Cooking Mode State
    private val _cookingState = MutableStateFlow(CookingState())
    val cookingState = _cookingState.asStateFlow()

    private var timerJob: Job? = null

    // Event notification (Snackbar / Toasts)
    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    // Base Flows
    val allRecipes: StateFlow<List<Recipe>> = repository.allRecipes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteRecipes: StateFlow<List<Recipe>> = repository.favoriteRecipes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shoppingItems: StateFlow<List<ShoppingItem>> = repository.allShoppingItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mealPlans: StateFlow<List<MealPlanItem>> = repository.allMealPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Recipes for Discover Screen
    val filteredRecipes: StateFlow<List<Recipe>> = combine(
        allRecipes,
        _searchQuery,
        _selectedCategory
    ) { recipes, query, category ->
        recipes.filter { recipe ->
            val matchesQuery = query.isBlank() ||
                recipe.title.contains(query, ignoreCase = true) ||
                recipe.description.contains(query, ignoreCase = true) ||
                recipe.ingredients.any { it.name.contains(query, ignoreCase = true) }

            val matchesCategory = when (category) {
                "All" -> true
                "Favorites" -> recipe.isFavorite
                "Quick" -> recipe.totalTimeMinutes <= 25 || recipe.category.equals("Quick", ignoreCase = true)
                else -> recipe.category.equals(category, ignoreCase = true)
            }

            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectRecipe(recipe: Recipe) {
        _selectedRecipe.value = recipe
        _servingsCount.value = recipe.servings
    }

    fun clearSelectedRecipe() {
        _selectedRecipe.value = null
    }

    fun adjustServings(delta: Int) {
        _selectedRecipe.value?.let {
            val newCount = (_servingsCount.value + delta).coerceIn(1, 24)
            _servingsCount.value = newCount
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.toggleFavorite(recipe.id, recipe.isFavorite)
            // Also update selected recipe if it's currently open
            if (_selectedRecipe.value?.id == recipe.id) {
                _selectedRecipe.update { it?.copy(isFavorite = !recipe.isFavorite) }
            }
        }
    }

    fun addIngredientsToShoppingList(recipe: Recipe) {
        viewModelScope.launch {
            val multiplier = _servingsCount.value.toDouble() / recipe.servings.toDouble()
            repository.addIngredientsToShoppingList(recipe.title, recipe.ingredients, multiplier)
            _userMessage.emit("Added ${recipe.ingredients.size} ingredients to your Grocery List!")
        }
    }

    fun toggleShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.toggleShoppingItem(item.id, !item.isCompleted)
        }
    }

    fun addCustomShoppingItem(name: String, quantity: String, category: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addCustomShoppingItem(name.trim(), quantity.trim(), category)
            _userMessage.emit("Added \"$name\" to Grocery List")
        }
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteShoppingItem(item)
        }
    }

    fun clearCompletedShopping() {
        viewModelScope.launch {
            repository.clearCompletedShopping()
            _userMessage.emit("Cleared completed grocery items")
        }
    }

    fun setMealPlan(day: String, meal: String, recipe: Recipe) {
        viewModelScope.launch {
            repository.setMealPlan(day, meal, recipe)
            _userMessage.emit("Added ${recipe.title} to $day $meal")
        }
    }

    fun removeMealPlan(id: Long) {
        viewModelScope.launch {
            repository.removeMealPlan(id)
        }
    }

    fun saveCustomRecipe(
        title: String,
        description: String,
        category: String,
        prepTime: Int,
        cookTime: Int,
        servings: Int,
        difficulty: String,
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int,
        ingredients: List<IngredientItem>,
        instructions: List<InstructionStep>
    ) {
        viewModelScope.launch {
            val newRecipe = Recipe(
                title = title.trim(),
                description = description.trim(),
                category = category,
                prepTimeMinutes = prepTime,
                cookTimeMinutes = cookTime,
                servings = servings,
                difficulty = difficulty,
                calories = calories,
                proteinGrams = protein,
                carbsGrams = carbs,
                fatGrams = fat,
                ingredients = ingredients,
                instructions = instructions,
                isFavorite = false,
                isCustom = true
            )
            repository.insertCustomRecipe(newRecipe)
            _userMessage.emit("Recipe \"${newRecipe.title}\" created successfully!")
        }
    }

    // --- Cooking Mode Methods ---

    fun startCookingMode(recipe: Recipe) {
        timerJob?.cancel()
        val initialTimerMinutes = recipe.instructions.firstOrNull()?.timerMinutes
        val initialSeconds = (initialTimerMinutes ?: 0) * 60

        _cookingState.value = CookingState(
            recipe = recipe,
            currentStepIndex = 0,
            timerSecondsRemaining = initialSeconds,
            timerTotalSeconds = initialSeconds,
            isTimerRunning = false,
            isTimerCompleted = false
        )
    }

    fun exitCookingMode() {
        timerJob?.cancel()
        _cookingState.value = CookingState()
    }

    fun nextCookingStep() {
        val current = _cookingState.value
        val nextIndex = current.currentStepIndex + 1
        if (nextIndex < current.totalSteps) {
            timerJob?.cancel()
            val stepTimer = current.recipe?.instructions?.getOrNull(nextIndex)?.timerMinutes
            val seconds = (stepTimer ?: 0) * 60
            _cookingState.value = current.copy(
                currentStepIndex = nextIndex,
                timerSecondsRemaining = seconds,
                timerTotalSeconds = seconds,
                isTimerRunning = false,
                isTimerCompleted = false
            )
        }
    }

    fun prevCookingStep() {
        val current = _cookingState.value
        val prevIndex = current.currentStepIndex - 1
        if (prevIndex >= 0) {
            timerJob?.cancel()
            val stepTimer = current.recipe?.instructions?.getOrNull(prevIndex)?.timerMinutes
            val seconds = (stepTimer ?: 0) * 60
            _cookingState.value = current.copy(
                currentStepIndex = prevIndex,
                timerSecondsRemaining = seconds,
                timerTotalSeconds = seconds,
                isTimerRunning = false,
                isTimerCompleted = false
            )
        }
    }

    fun toggleCookingTimer() {
        val current = _cookingState.value
        if (current.isTimerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    fun startTimer() {
        val current = _cookingState.value
        if (current.timerSecondsRemaining <= 0) return

        timerJob?.cancel()
        _cookingState.update { it.copy(isTimerRunning = true, isTimerCompleted = false) }

        timerJob = viewModelScope.launch {
            while (_cookingState.value.timerSecondsRemaining > 0 && _cookingState.value.isTimerRunning) {
                delay(1000L)
                _cookingState.update { state ->
                    val updated = state.timerSecondsRemaining - 1
                    if (updated <= 0) {
                        triggerVibration()
                        state.copy(
                            timerSecondsRemaining = 0,
                            isTimerRunning = false,
                            isTimerCompleted = true
                        )
                    } else {
                        state.copy(timerSecondsRemaining = updated)
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _cookingState.update { it.copy(isTimerRunning = false) }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _cookingState.update { state ->
            val stepMinutes = state.currentStep?.timerMinutes ?: 0
            val totalSec = stepMinutes * 60
            state.copy(
                timerSecondsRemaining = totalSec,
                timerTotalSeconds = totalSec,
                isTimerRunning = false,
                isTimerCompleted = false
            )
        }
    }

    fun addTimerMinutes(minutes: Int) {
        _cookingState.update { state ->
            val added = minutes * 60
            val newSec = state.timerSecondsRemaining + added
            state.copy(
                timerSecondsRemaining = newSec,
                timerTotalSeconds = maxOf(state.timerTotalSeconds, newSec),
                isTimerCompleted = false
            )
        }
    }

    private fun triggerVibration() {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createWaveform(longArrayOf(0, 400, 200, 400), -1)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(
                        VibrationEffect.createWaveform(longArrayOf(0, 400, 200, 400), -1)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(longArrayOf(0, 400, 200, 400), -1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
