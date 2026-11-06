package com.example.data.local

import com.example.data.model.IngredientItem
import com.example.data.model.InstructionStep
import com.example.data.model.Recipe

object PreloadedRecipes {
    val items: List<Recipe> = listOf(
        Recipe(
            id = 1,
            title = "Creamy Tuscan Garlic Chicken Pasta",
            description = "Tender pan-seared chicken cutlets in a rich sun-dried tomato, garlic, and baby spinach parmesan cream sauce tossed with fettuccine.",
            category = "Dinner",
            prepTimeMinutes = 10,
            cookTimeMinutes = 20,
            servings = 4,
            difficulty = "Medium",
            calories = 580,
            proteinGrams = 42,
            carbsGrams = 48,
            fatGrams = 24,
            ingredients = listOf(
                IngredientItem("Chicken breast (cut into cutlets)", 500.0, "g"),
                IngredientItem("Fettuccine or penne pasta", 300.0, "g"),
                IngredientItem("Heavy cream", 240.0, "ml"),
                IngredientItem("Chicken broth", 120.0, "ml"),
                IngredientItem("Sun-dried tomatoes (chopped)", 100.0, "g"),
                IngredientItem("Fresh baby spinach", 150.0, "g"),
                IngredientItem("Grated Parmesan cheese", 60.0, "g"),
                IngredientItem("Garlic cloves (minced)", 4.0, "cloves"),
                IngredientItem("Olive oil", 2.0, "tbsp"),
                IngredientItem("Italian seasoning & cracked pepper", 1.0, "tsp")
            ),
            instructions = listOf(
                InstructionStep(1, "Bring a large pot of salted water to a rolling boil. Cook pasta al dente according to package instructions, drain and reserve 1/2 cup pasta water.", timerMinutes = 10),
                InstructionStep(2, "Season chicken cutlets with Italian herbs, salt, and pepper. Heat 1 tbsp olive oil in a skillet over medium-high heat."),
                InstructionStep(3, "Sear chicken until golden brown and cooked through (about 5-6 minutes per side). Transfer to a warm plate and slice into strips.", timerMinutes = 6),
                InstructionStep(4, "In the same skillet, add remaining olive oil and minced garlic; sauté for 60 seconds until fragrant.", timerMinutes = 1),
                InstructionStep(5, "Pour in chicken broth, heavy cream, and chopped sun-dried tomatoes. Bring to a gentle simmer for 4 minutes until sauce begins to thicken.", timerMinutes = 4),
                InstructionStep(6, "Stir in fresh spinach and grated parmesan until spinach wilts. Toss in cooked pasta and sliced chicken, garnish with fresh basil and extra parmesan.")
            ),
            isFavorite = true
        ),
        Recipe(
            id = 2,
            title = "15-Minute Avocado Lime Shrimp Tacos",
            description = "Succulent chili-lime glazed shrimp tucked inside warm corn tortillas, topped with creamy avocado wedges, crunchy slaw, and cilantro drizzle.",
            category = "Quick",
            prepTimeMinutes = 5,
            cookTimeMinutes = 10,
            servings = 3,
            difficulty = "Easy",
            calories = 360,
            proteinGrams = 28,
            carbsGrams = 32,
            fatGrams = 14,
            ingredients = listOf(
                IngredientItem("Large raw shrimp (peeled & deveined)", 400.0, "g"),
                IngredientItem("Small corn or flour tortillas", 6.0, "pcs"),
                IngredientItem("Ripe avocados (sliced)", 2.0, "pcs"),
                IngredientItem("Purple cabbage slaw", 150.0, "g"),
                IngredientItem("Lime (juiced)", 2.0, "pcs"),
                IngredientItem("Smoked paprika & ground cumin", 1.5, "tsp"),
                IngredientItem("Olive oil", 1.5, "tbsp"),
                IngredientItem("Fresh cilantro (chopped)", 3.0, "tbsp")
            ),
            instructions = listOf(
                InstructionStep(1, "In a medium bowl, toss shrimp with smoked paprika, cumin, a pinch of chili flakes, salt, and 1 tbsp olive oil."),
                InstructionStep(2, "Heat a large cast-iron skillet over medium-high heat. Add remaining oil and cook shrimp for 2-3 minutes per side until pink and slightly charred.", timerMinutes = 3),
                InstructionStep(3, "Warm tortillas on a dry skillet or over an open flame for 30 seconds per side until pliable.", timerMinutes = 1),
                InstructionStep(4, "Assemble tacos by layering cabbage slaw, avocado slices, and hot glazed shrimp into each tortilla. Squeeze fresh lime juice and scatter cilantro.")
            ),
            isFavorite = true
        ),
        Recipe(
            id = 3,
            title = "Mediterranean Quinoa Power Bowl",
            description = "Nutritious and vibrant grain bowl loaded with fluffy quinoa, crisp Persian cucumbers, kalamata olives, cherry tomatoes, and crumbled feta cheese with lemon herb vinaigrette.",
            category = "Vegetarian",
            prepTimeMinutes = 10,
            cookTimeMinutes = 15,
            servings = 2,
            difficulty = "Easy",
            calories = 420,
            proteinGrams = 16,
            carbsGrams = 52,
            fatGrams = 18,
            ingredients = listOf(
                IngredientItem("White or tri-color quinoa (uncooked)", 180.0, "g"),
                IngredientItem("Cherry tomatoes (halved)", 150.0, "g"),
                IngredientItem("Persian cucumbers (diced)", 2.0, "pcs"),
                IngredientItem("Kalamata olives (pitted)", 60.0, "g"),
                IngredientItem("Crumbled sheep's feta cheese", 80.0, "g"),
                IngredientItem("Canned chickpeas (rinsed & drained)", 200.0, "g"),
                IngredientItem("Extra virgin olive oil", 2.0, "tbsp"),
                IngredientItem("Lemon juice", 2.0, "tbsp"),
                IngredientItem("Fresh oregano & mint leaves", 2.0, "tbsp")
            ),
            instructions = listOf(
                InstructionStep(1, "Rinse quinoa. Combine with 360ml water in a saucepan with a pinch of salt. Bring to a boil, reduce to low, cover and simmer for 15 minutes.", timerMinutes = 15),
                InstructionStep(2, "Remove quinoa from heat and let sit covered for 5 minutes. Fluff with a fork and let cool slightly.", timerMinutes = 5),
                InstructionStep(3, "In a small mason jar or bowl, whisk olive oil, lemon juice, dried oregano, salt, and black pepper for the vinaigrette."),
                InstructionStep(4, "Divide quinoa into two bowls. Arrange cherry tomatoes, diced cucumbers, olives, chickpeas, and crumbled feta in colorful sections."),
                InstructionStep(5, "Drizzle with lemon vinaigrette and top with fresh mint before serving warm or chilled.")
            ),
            isFavorite = false
        ),
        Recipe(
            id = 4,
            title = "Fluffy Blueberry Buttermilk Pancakes",
            description = "Cloud-like, golden buttermilk pancakes bursting with sweet summer blueberries, served with warm pure maple syrup and whipped butter.",
            category = "Breakfast",
            prepTimeMinutes = 10,
            cookTimeMinutes = 12,
            servings = 4,
            difficulty = "Easy",
            calories = 340,
            proteinGrams = 9,
            carbsGrams = 56,
            fatGrams = 10,
            ingredients = listOf(
                IngredientItem("All-purpose flour", 250.0, "g"),
                IngredientItem("Buttermilk (or milk with 1 tbsp lemon)", 300.0, "ml"),
                IngredientItem("Fresh organic blueberries", 150.0, "g"),
                IngredientItem("Baking powder", 2.0, "tsp"),
                IngredientItem("Baking soda", 0.5, "tsp"),
                IngredientItem("Cane sugar", 2.0, "tbsp"),
                IngredientItem("Large eggs", 2.0, "pcs"),
                IngredientItem("Melted butter", 45.0, "g"),
                IngredientItem("Pure vanilla extract", 1.0, "tsp")
            ),
            instructions = listOf(
                InstructionStep(1, "Whisk flour, sugar, baking powder, baking soda, and a pinch of fine sea salt in a large bowl."),
                InstructionStep(2, "In a separate jug, whisk buttermilk, eggs, melted butter, and vanilla extract until smooth."),
                InstructionStep(3, "Pour wet ingredients into dry ingredients and fold gently with a spatula just until combined (do not overmix; lumps are normal)."),
                InstructionStep(4, "Heat a nonstick griddle or skillet over medium heat. Lightly brush with butter. Pour 1/4 cup batter per pancake and drop blueberries on top."),
                InstructionStep(5, "Cook for 2-3 minutes until bubbles form on the surface and pop. Flip gently and cook for another 1-2 minutes until golden brown.", timerMinutes = 3),
                InstructionStep(6, "Stack high and serve immediately with pure maple syrup and a knob of butter.")
            ),
            isFavorite = true
        ),
        Recipe(
            id = 5,
            title = "Authentic Shoyu Ramen with Ajitsuke Tamago",
            description = "Rich artisanal chicken-dashi shoyu broth served with chewy ramen noodles, tender braised pork belly, ramen egg, nori, and scallions.",
            category = "Dinner",
            prepTimeMinutes = 15,
            cookTimeMinutes = 25,
            servings = 2,
            difficulty = "Medium",
            calories = 620,
            proteinGrams = 34,
            carbsGrams = 68,
            fatGrams = 22,
            ingredients = listOf(
                IngredientItem("Fresh or dried ramen noodles", 240.0, "g"),
                IngredientItem("Rich chicken stock or dashi", 800.0, "ml"),
                IngredientItem("Japanese soy sauce (Shoyu)", 3.0, "tbsp"),
                IngredientItem("Mirin", 2.0, "tbsp"),
                IngredientItem("Toasted sesame oil", 1.0, "tbsp"),
                IngredientItem("Garlic (grated)", 2.0, "cloves"),
                IngredientItem("Ginger (grated)", 1.0, "tsp"),
                IngredientItem("Soft-boiled eggs (marinated)", 2.0, "pcs"),
                IngredientItem("Green scallions (thinly sliced)", 3.0, "stalks"),
                IngredientItem("Nori sheets & bamboo shoots", 2.0, "sheets")
            ),
            instructions = listOf(
                InstructionStep(1, "To prepare soft-boiled eggs, boil large eggs for exactly 6 minutes 30 seconds, then immediately plunge into an ice water bath. Peel and marinate in soy-mirin mixture.", timerMinutes = 7),
                InstructionStep(2, "In a soup pot, heat sesame oil over medium heat. Sauté grated garlic and ginger for 1 minute until fragrant.", timerMinutes = 1),
                InstructionStep(3, "Pour in chicken stock, soy sauce, and mirin. Bring to a gentle boil, then simmer covered for 10 minutes to blend flavors.", timerMinutes = 10),
                InstructionStep(4, "In a separate pot, cook ramen noodles in boiling water for 2-3 minutes until al dente. Drain thoroughly.", timerMinutes = 3),
                InstructionStep(5, "Divide noodles between two deep ramen bowls. Ladle hot shoyu broth over noodles. Top with halved marinated eggs, sliced scallions, and nori.")
            ),
            isFavorite = false
        ),
        Recipe(
            id = 6,
            title = "Warm Molten Chocolate Lava Cake",
            description = "Decadent dark chocolate individual cakes with an irresistibly gooey, molten chocolate center that flows when sliced, dusted with powdered sugar.",
            category = "Dessert",
            prepTimeMinutes = 10,
            cookTimeMinutes = 14,
            servings = 2,
            difficulty = "Medium",
            calories = 460,
            proteinGrams = 7,
            carbsGrams = 42,
            fatGrams = 30,
            ingredients = listOf(
                IngredientItem("70% Bittersweet dark chocolate", 120.0, "g"),
                IngredientItem("Unsalted butter", 80.0, "g"),
                IngredientItem("Powdered sugar", 60.0, "g"),
                IngredientItem("Large whole eggs", 2.0, "pcs"),
                IngredientItem("Egg yolk", 1.0, "pc"),
                IngredientItem("All-purpose flour", 30.0, "g"),
                IngredientItem("Espresso powder", 0.5, "tsp"),
                IngredientItem("Pinch of sea salt", 1.0, "pinch")
            ),
            instructions = listOf(
                InstructionStep(1, "Preheat oven to 220°C (425°F). Butter two 6-oz ramekins and dust thoroughly with cocoa powder or flour, tapping out excess."),
                InstructionStep(2, "Chop chocolate and butter. Melt together in a heatproof bowl set over simmering water (or in microwave in 30-sec bursts), stirring until silky smooth.", timerMinutes = 3),
                InstructionStep(3, "Whisk in powdered sugar, whole eggs, egg yolk, espresso powder, and pinch of salt until well incorporated."),
                InstructionStep(4, "Gently fold in flour just until no dry pockets remain. Divide batter evenly between prepared ramekins."),
                InstructionStep(5, "Bake for 12 to 14 minutes until sides are firm and set, but the center is still soft and slightly jiggly.", timerMinutes = 13),
                InstructionStep(6, "Cool for 1 minute, run a knife around edges, invert onto dessert plates, and serve immediately with vanilla bean ice cream.")
            ),
            isFavorite = true
        )
    )
}
