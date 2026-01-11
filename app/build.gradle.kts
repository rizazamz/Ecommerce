plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.riza.ecommerce"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.riza.ecommerce"
        minSdk = 25
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
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
    implementation(libs.coordinatorlayout)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.swiperefresh)
    implementation(libs.recyclerview)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    testImplementation(libs.mockito.core)
    testImplementation(libs.arch.core.testing)
    testImplementation(libs.okhttp.mockwebserver)

    implementation("com.airbnb.android:lottie:6.1.0")
}