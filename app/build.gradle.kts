plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.drugtrackerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.drugtrackerapp"
        minSdk = 24
        targetSdk = 35
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
    //Data binding
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //Content size library
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    // Add the dependency for the Firebase Authentication library
    implementation(libs.google.firebase.auth)
    //alertToast
    implementation(libs.alerter)
    //Circle image
    implementation(libs.circleimageview)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.runtime)
}