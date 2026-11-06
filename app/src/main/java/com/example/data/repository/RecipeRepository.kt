package com.example.data.repository

import com.example.data.local.MealPlanDao
import com.example.data.local.MealPlanEntity
import com.example.data.local.PreloadedRecipes
import com.example.data.local.RecipeDao
import com.example.data.local.RecipeEntity
import com.example.data.local.ShoppingDao
import com.example.data.local.ShoppingItemEntity
import com.example.data.model.IngredientItem
import com.example.data.model.MealPlanItem
import com.example.data.model.Recipe
import com.example.data.model.ShoppingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val shoppingDao: ShoppingDao,
    private val mealPlanDao: MealPlanDao
) {
    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes().map { list ->
        list.map { it.toDomain() }
    }

    val favoriteRecipes: Flow<List<Recipe>> = recipeDao.getFavoriteRecipes().map { list ->
        list.map { it.toDomain() }
    }

    val allShoppingItems: Flow<List<ShoppingItem>> = shoppingDao.getAllShoppingItems().map { list ->
        list.map { it.toDomain() }
    }

    val allMealPlans: Flow<List<MealPlanItem>> = mealPlanDao.getAllMealPlans().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        val count = recipeDao.getRecipeCount()
        if (count == 0) {
            val entities = PreloadedRecipes.items.map { RecipeEntity.fromDomain(it) }
            recipeDao.insertRecipes(entities)
        }
    }

    suspend fun toggleFavorite(recipeId: Long, currentFavorite: Boolean) = withContext(Dispatchers.IO) {
        recipeDao.updateFavorite(recipeId, !currentFavorite)
    }

    suspend fun insertCustomRecipe(recipe: Recipe): Long = withContext(Dispatchers.IO) {
        val entity = RecipeEntity.fromDomain(recipe.copy(isCustom = true))
        recipeDao.insertRecipe(entity)
    }

    suspend fun deleteRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        recipeDao.deleteRecipe(RecipeEntity.fromDomain(recipe))
    }

    suspend fun addIngredientsToShoppingList(
        recipeTitle: String,
        ingredients: List<IngredientItem>,
        multiplier: Double
    ) = withContext(Dispatchers.IO) {
        val entities = ingredients.map { ing ->
            ShoppingItemEntity(
                name = ing.name,
                quantity = ing.getFormattedQuantity(multiplier),
                category = categorizeIngredient(ing.name),
                isCompleted = false,
                recipeOrigin = recipeTitle
            )
        }
        shoppingDao.insertItems(entities)
    }

    suspend fun addCustomShoppingItem(name: String, quantity: String, category: String) = withContext(Dispatchers.IO) {
        shoppingDao.insertItem(
            ShoppingItemEntity(
                name = name,
                quantity = quantity,
                category = category,
                isCompleted = false,
                recipeOrigin = "Personal Note"
            )
        )
    }

    suspend fun toggleShoppingItem(id: Long, isCompleted: Boolean) = withContext(Dispatchers.IO) {
        shoppingDao.updateCompleted(id, isCompleted)
    }

    suspend fun deleteShoppingItem(item: ShoppingItem) = withContext(Dispatchers.IO) {
        shoppingDao.deleteItem(ShoppingItemEntity.fromDomain(item))
    }

    suspend fun clearCompletedShopping() = withContext(Dispatchers.IO) {
        shoppingDao.clearCompleted()
    }

    suspend fun setMealPlan(dayOfWeek: String, mealType: String, recipe: Recipe) = withContext(Dispatchers.IO) {
        mealPlanDao.deleteMealPlanByDayAndType(dayOfWeek, mealType)
        mealPlanDao.insertMealPlan(
            MealPlanEntity(
                dayOfWeek = dayOfWeek,
                mealType = mealType,
                recipeId = recipe.id,
                recipeTitle = recipe.title,
                prepTimeMinutes = recipe.prepTimeMinutes
            )
        )
    }

    suspend fun removeMealPlan(id: Long) = withContext(Dispatchers.IO) {
        mealPlanDao.deleteMealPlanById(id)
    }

    private fun categorizeIngredient(name: String): String {
        val lower = name.lowercase()
        return when {
            lower.contains("chicken") || lower.contains("pork") || lower.contains("beef") ||
            lower.contains("shrimp") || lower.contains("salmon") || lower.contains("meat") -> "Meat & Seafood"
            lower.contains("cream") || lower.contains("cheese") || lower.contains("milk") ||
            lower.contains("butter") || lower.contains("egg") || lower.contains("yogurt") -> "Dairy & Eggs"
            lower.contains("garlic") || lower.contains("onion") || lower.contains("spinach") ||
            lower.contains("tomato") || lower.contains("avocado") || lower.contains("lime") ||
            lower.contains("lemon") || lower.contains("cilantro") || lower.contains("cucumber") ||
            lower.contains("blueberry") || lower.contains("scallion") || lower.contains("mint") -> "Fresh Produce"
            lower.contains("pasta") || lower.contains("flour") || lower.contains("sugar") ||
            lower.contains("quinoa") || lower.contains("oil") || lower.contains("soy sauce") ||
            lower.contains("mirin") || lower.contains("broth") || lower.contains("chocolate") -> "Pantry & Baking"
            else -> "Spices & Seasonings"
        }
    }
}
