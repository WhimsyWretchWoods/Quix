plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21"
}


android {
    namespace = "com.quix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.quix"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    signingConfigs {
    create("release") {
        storeFile = file(project.property("storeFile") as String)
        storePassword = project.property("storePassword") as String
        keyAlias = project.property("keyAlias") as String
        keyPassword = project.property("keyPassword") as String
    }
}

buildTypes {
    getByName("release") {
        signingConfig = signingConfigs.getByName("release")
        isMinifyEnabled = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.1.21"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui:1.8.2")
    implementation("androidx.compose.ui:ui-graphics:1.8.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.8.2")
    implementation("androidx.compose.material3:material3:1.3.2")
    debugImplementation("androidx.compose.ui:ui-tooling:1.8.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("io.coil-kt:coil-compose:2.7.0")


}
