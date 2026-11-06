package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.JsonConverters
import com.example.data.model.MealPlanItem
import com.example.data.model.Recipe
import com.example.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: String,
    val calories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val ingredientsJson: String,
    val instructionsJson: String,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val imageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Recipe {
        return Recipe(
            id = id,
            title = title,
            description = description,
            category = category,
            prepTimeMinutes = prepTimeMinutes,
            cookTimeMinutes = cookTimeMinutes,
            servings = servings,
            difficulty = difficulty,
            calories = calories,
            proteinGrams = proteinGrams,
            carbsGrams = carbsGrams,
            fatGrams = fatGrams,
            ingredients = JsonConverters.jsonToIngredients(ingredientsJson),
            instructions = JsonConverters.jsonToInstructions(instructionsJson),
            isFavorite = isFavorite,
            isCustom = isCustom,
            imageUrl = imageUrl
        )
    }

    companion object {
        fun fromDomain(recipe: Recipe): RecipeEntity {
            return RecipeEntity(
                id = recipe.id,
                title = recipe.title,
                description = recipe.description,
                category = recipe.category,
                prepTimeMinutes = recipe.prepTimeMinutes,
                cookTimeMinutes = recipe.cookTimeMinutes,
                servings = recipe.servings,
                difficulty = recipe.difficulty,
                calories = recipe.calories,
                proteinGrams = recipe.proteinGrams,
                carbsGrams = recipe.carbsGrams,
                fatGrams = recipe.fatGrams,
                ingredientsJson = JsonConverters.ingredientsToJson(recipe.ingredients),
                instructionsJson = JsonConverters.instructionsToJson(recipe.instructions),
                isFavorite = recipe.isFavorite,
                isCustom = recipe.isCustom,
                imageUrl = recipe.imageUrl
            )
        }
    }
}

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: String,
    val category: String,
    val isCompleted: Boolean = false,
    val recipeOrigin: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain() = ShoppingItem(
        id = id,
        name = name,
        quantity = quantity,
        category = category,
        isCompleted = isCompleted,
        recipeOrigin = recipeOrigin
    )

    companion object {
        fun fromDomain(item: ShoppingItem) = ShoppingItemEntity(
            id = item.id,
            name = item.name,
            quantity = item.quantity,
            category = item.category,
            isCompleted = item.isCompleted,
            recipeOrigin = item.recipeOrigin
        )
    }
}

@Entity(tableName = "meal_plans")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayOfWeek: String,
    val mealType: String,
    val recipeId: Long,
    val recipeTitle: String,
    val prepTimeMinutes: Int,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain() = MealPlanItem(
        id = id,
        dayOfWeek = dayOfWeek,
        mealType = mealType,
        recipeId = recipeId,
        recipeTitle = recipeTitle,
        prepTimeMinutes = prepTimeMinutes
    )

    companion object {
        fun fromDomain(plan: MealPlanItem) = MealPlanEntity(
            id = plan.id,
            dayOfWeek = plan.dayOfWeek,
            mealType = plan.mealType,
            recipeId = plan.recipeId,
            recipeTitle = plan.recipeTitle,
            prepTimeMinutes = plan.prepTimeMinutes
        )
    }
}

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    fun getRecipeById(id: Long): Flow<RecipeEntity?>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getRecipeByIdOnce(id: Long): RecipeEntity?

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun getRecipeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
}

@Dao
interface ShoppingDao {
    @Query("SELECT * FROM shopping_items ORDER BY isCompleted ASC, id DESC")
    fun getAllShoppingItems(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShoppingItemEntity>)

    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Query("UPDATE shopping_items SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompleted(id: Long, isCompleted: Boolean)

    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)

    @Query("DELETE FROM shopping_items WHERE isCompleted = 1")
    suspend fun clearCompleted()
}

@Dao
interface MealPlanDao {
    @Query("SELECT * FROM meal_plans ORDER BY id ASC")
    fun getAllMealPlans(): Flow<List<MealPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(mealPlan: MealPlanEntity): Long

    @Query("DELETE FROM meal_plans WHERE id = :id")
    suspend fun deleteMealPlanById(id: Long)

    @Query("DELETE FROM meal_plans WHERE dayOfWeek = :day AND mealType = :meal")
    suspend fun deleteMealPlanByDayAndType(day: String, meal: String)
}
