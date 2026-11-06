# FlavorCraft 🍳

A modern, intuitive Android Recipe Application and Culinary Companion built with **Jetpack Compose**, **Kotlin Coroutines**, and **Room Database**.

---

## ✨ Features

- **Curated Recipe Collection**: Diverse recipes across categories including Breakfast, Lunch, Dinner, Desserts, Vegetarian, and Quick 15-Minute Meals.
- **Dynamic Servings Scaler**: Interactive stepper (`-` / `+`) that automatically recalculates and scales all ingredient quantities in real-time.
- **Interactive Step-by-Step Cooking Mode**: Distraction-free kitchen focus mode with large typography, step progress bar, and built-in cooking timers with vibration alerts.
- **Smart Grocery List**: One-tap addition of recipe ingredients to your shopping list, categorized by produce, dairy, meat, and pantry items, with strike-through checklists and badge notifications.
- **Weekly Meal Planner**: Organize and schedule your meals across Monday to Sunday for Breakfast, Lunch, and Dinner with quick shortcuts to launch cooking mode.
- **Custom Recipe Creator**: Easily add and save your family recipes with customized ingredients, nutritional info, and timed instructions stored securely in the local Room database.
- **Nutritional Insights**: Track calories, protein, carbohydrates, and fat per serving for every dish.
- **Search & Filter**: Real-time multi-criteria filtering by title, ingredients, or dietary preferences.

---

## 🛠️ Tech Stack & Architecture

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3 (M3)
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Local Persistence**: [Android Room Database](https://developer.android.com/training/data-storage/room) with reactive `Flow` queries and KSP
- **Image Loading**: Coil Compose & Vector Drawables
- **Concurrency**: Kotlin Coroutines & Kotlin StateFlow / SharedFlow
- **Minimum SDK**: Android API 24+ (Android 7.0 Nougat)
- **Target SDK**: Android API 36 (Android 16)

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/mbhargavi802-source/Recipe-application.git
   ```
2. Open the project in **Android Studio Ladybug (or newer)**.
3. Allow Gradle to sync dependencies.
4. Run the app on an Android device or emulator running API 24+.

---

## 📄 License
This project is open-source under the Apache 2.0 License.
