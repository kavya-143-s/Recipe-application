package com.example.data.model

import org.json.JSONArray
import org.json.JSONObject

data class IngredientItem(
    val name: String,
    val amount: Double,
    val unit: String
) {
    fun getFormattedQuantity(servingMultiplier: Double = 1.0): String {
        val scaledAmount = amount * servingMultiplier
        val formattedNumber = if (scaledAmount == scaledAmount.toLong().toDouble()) {
            scaledAmount.toLong().toString()
        } else {
            String.format("%.1f", scaledAmount)
        }
        return if (unit.isBlank()) formattedNumber else "$formattedNumber $unit"
    }
}

data class InstructionStep(
    val stepNumber: Int,
    val instruction: String,
    val timerMinutes: Int? = null // Extracted or designated timer for this step
)

data class Recipe(
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: String, // Easy, Medium, Hard
    val calories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val ingredients: List<IngredientItem>,
    val instructions: List<InstructionStep>,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val imageUrl: String? = null
) {
    val totalTimeMinutes: Int get() = prepTimeMinutes + cookTimeMinutes
}

data class ShoppingItem(
    val id: Long = 0,
    val name: String,
    val quantity: String,
    val category: String,
    val isCompleted: Boolean = false,
    val recipeOrigin: String? = null
)

data class MealPlanItem(
    val id: Long = 0,
    val dayOfWeek: String, // Monday, Tuesday, ...
    val mealType: String,  // Breakfast, Lunch, Dinner
    val recipeId: Long,
    val recipeTitle: String,
    val prepTimeMinutes: Int
)

object JsonConverters {
    fun ingredientsToJson(ingredients: List<IngredientItem>): String {
        val array = JSONArray()
        ingredients.forEach { item ->
            val obj = JSONObject().apply {
                put("name", item.name)
                put("amount", item.amount)
                put("unit", item.unit)
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun jsonToIngredients(json: String): List<IngredientItem> {
        val list = mutableListOf<IngredientItem>()
        if (json.isBlank()) return list
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    IngredientItem(
                        name = obj.optString("name", ""),
                        amount = obj.optDouble("amount", 1.0),
                        unit = obj.optString("unit", "")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun instructionsToJson(instructions: List<InstructionStep>): String {
        val array = JSONArray()
        instructions.forEach { step ->
            val obj = JSONObject().apply {
                put("stepNumber", step.stepNumber)
                put("instruction", step.instruction)
                if (step.timerMinutes != null) {
                    put("timerMinutes", step.timerMinutes)
                }
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun jsonToInstructions(json: String): List<InstructionStep> {
        val list = mutableListOf<InstructionStep>()
        if (json.isBlank()) return list
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val timer = if (obj.has("timerMinutes")) obj.getInt("timerMinutes") else null
                list.add(
                    InstructionStep(
                        stepNumber = obj.optInt("stepNumber", i + 1),
                        instruction = obj.optString("instruction", ""),
                        timerMinutes = timer
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
