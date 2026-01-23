
// Wtyczki (Plugins) - zamienione z 'alias(libs...)' na bezpośrednie 'id("...")'
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.dzialajproszelodowka"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.dzialajproszelodowka"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

}

// Przeniosłem definicje wersji POZA blok 'dependencies'
// To jest czystsza i bezpieczniejsza praktyka.
val core_ktx_version = "1.13.1"
val lifecycle_version = "2.8.3" // Użyte dla runtime-ktx, viewmodel-ktx, etc.
val activity_compose_version = "1.9.0"
val compose_bom_version = "2024.06.00"
val junit_version = "4.13.2"
val androidx_junit_version = "1.2.1"
val espresso_version = "3.6.1"

val room_version = "2.6.1"
val coroutines_version = "1.8.1"
val nav_version = "2.7.7"
val retrofit_version = "2.11.0"
val work_version = "2.9.0"
val mlkit_version = "17.2.0"
val icons_extended_version = "1.6.8" // Ta linia, którą dodaliśmy do ikon


dependencies {
    // Istniejące zależności Core i Compose - ZAMIENIONE NA TEKST
    implementation("androidx.core:core-ktx:$core_ktx_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.activity:activity-compose:$activity_compose_version")
    implementation(platform("androidx.compose:compose-bom:$compose_bom_version"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation(libs.androidx.compose.remote.creation.core) // Material Design 3

    // Zależności testowe - ZAMIENIONE NA TEKST
    testImplementation("junit:junit:$junit_version")
    androidTestImplementation("androidx.test.ext:junit:$androidx_junit_version")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso_version")
    androidTestImplementation(platform("androidx.compose:compose-bom:$compose_bom_version"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // --- NOWE ZALEŻNOŚCI DLA SMARTFRIDGE ---
    // Ta sekcja była już poprawna, bo używała zmiennych

    // Room (Baza danych)
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Ikony (które dodaliśmy wcześniej)
    implementation("androidx.compose.material:material-icons-extended:$icons_extended_version")

    // ViewModel & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    // Navigation (Nawigacja w Compose - zamiast Fragmentów)
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Networking (Retrofit + Gson)
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version") // Lub converter-moshi

    // WorkManager (Zadania w tle, powiadomienia)
    implementation("androidx.work:work-runtime-ktx:$work_version")

    // ML Kit (Skaner kodów kreskowych)
    implementation("com.google.mlkit:barcode-scanning:$mlkit_version")

    // CameraX (Kamera)
    val camerax_version = "1.3.4"
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    val media3_version = "1.3.1"
    implementation("androidx.media3:media3-exoplayer:$media3_version")
    implementation("androidx.media3:media3-ui:$media3_version")
    implementation("androidx.media3:media3-common:$media3_version")

    // do testów
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("junit:junit:4.13.2")

    // do testów UI
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


}