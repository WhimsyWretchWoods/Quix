# Keep the main entry point
-keep class com.quix.MainActivity { *; }

# Keep all composables
-keep class androidx.compose.** { *; }
-keep class androidx.activity.** { *; }

# Optional: Log if anything crashes during shrinking
-dontwarn kotlinx.coroutines.**
