package com.example

import com.example.data.model.IngredientItem
import com.example.data.model.InstructionStep
import com.example.data.model.JsonConverters
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun ingredientItem_scalesProperly() {
        val ingredient = IngredientItem("Pasta", 200.0, "g")
        assertEquals("200 g", ingredient.getFormattedQuantity(1.0))
        assertEquals("400 g", ingredient.getFormattedQuantity(2.0))
        assertEquals("100 g", ingredient.getFormattedQuantity(0.5))
    }

    @Test
    fun jsonConverters_roundTripIngredients() {
        val list = listOf(
            IngredientItem("Olive oil", 2.0, "tbsp"),
            IngredientItem("Garlic", 4.0, "cloves")
        )
        val json = JsonConverters.ingredientsToJson(list)
        val deserialized = JsonConverters.jsonToIngredients(json)
        assertEquals(2, deserialized.size)
        assertEquals("Olive oil", deserialized[0].name)
        assertEquals(2.0, deserialized[0].amount, 0.001)
        assertEquals("tbsp", deserialized[0].unit)
    }

    @Test
    fun jsonConverters_roundTripInstructions() {
        val list = listOf(
            InstructionStep(1, "Boil water", timerMinutes = 10),
            InstructionStep(2, "Cook pasta", timerMinutes = null)
        )
        val json = JsonConverters.instructionsToJson(list)
        val deserialized = JsonConverters.jsonToInstructions(json)
        assertEquals(2, deserialized.size)
        assertEquals(10, deserialized[0].timerMinutes)
        assertEquals(null, deserialized[1].timerMinutes)
    }
}
