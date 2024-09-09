plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
  //  id("com.google.gms.google-services") // Add this line
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.rabapp.zakhrapha"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rabapp.zakhrapha"
        minSdk = 23
        targetSdk = 34
        versionCode = 3
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {


    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation ("androidx.fragment:fragment-ktx:1.4.1")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("com.google.android.play:review:2.0.1") // Replace with the latest version
    implementation ("com.google.android.play:review-ktx:2.0.1")
    //implementation("com.google.android.gms:play-services-ads:22.5.0") // Use the latest version
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("com.revenuecat.purchases:purchases:8.7.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation ("androidx.room:room-ktx:2.6.1") // KTX extension for coroutines

    kapt("androidx.room:room-compiler:2.6.1") // Needed for Kotlin projects
    implementation (libs.androidx.core.ktx)
    implementation ("com.asksira.android:loopingviewpager:1.4.1")

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
   // implementation(libs.play.services.ads.lite) // Or latest version
    implementation("com.google.android.gms:play-services-ads:23.3.0")
    implementation (libs.kotlinx.coroutines.core)


    // Coroutines Android (for use with ViewModel and LiveData)
    implementation (libs.kotlinx.coroutines.android)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
    implementation ("me.relex:circleindicator:2.1.6")

    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}