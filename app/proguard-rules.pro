# Keep Coil classes
-keep class coil.** { *; }
-dontwarn coil.**

# Keep Jetpack Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Required for reflection
-keepclassmembers class ** {
    @androidx.annotation.Keep *;
}

# Keep MainActivity and anything using @Composable
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Don’t strip app entry points
-keep class com.quix.MainActivity { *; }

# Prevent obfuscation of generated classes
-keepclassmembers class **.R$* {
    public static <fields>;
}
