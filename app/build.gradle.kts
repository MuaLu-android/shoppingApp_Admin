plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.maneger.appbanhang"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maneger.appbanhang"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    packagingOptions{
        exclude("META-INF/DEPENDENCIES")
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)
    implementation(fileTree(mapOf(
        "dir" to "D:\\TT_DA\\zalopay",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //RxJava
    implementation ("io.reactivex.rxjava3:rxjava:3.1.3")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Chuyển đổi JSON (gson) cho Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Chuyển đổi XML (nếu cần) cho Retrofit
    implementation ("com.squareup.retrofit2:converter-simplexml:2.9.0")

    // (Tùy chọn) Để hỗ trợ RxJava với Retrofit
    implementation ("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    //badge
    implementation ("com.nex3z:notification-badge:1.0.4")
    //evenbus
    implementation("org.greenrobot:eventbus:3.3.1")
    //paper
    implementation ("io.github.pilgr:paperdb:2.7.2")
    //Gson
    implementation ("com.google.code.gson:gson:2.11.0")
    //lottie
    implementation ("com.airbnb.android:lottie:6.5.2")
    //neumophism
    implementation ("com.github.fornewid:neumorphism:0.3.2")
    //imagepicker
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    //auth-token
    implementation ("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    //
    implementation ("com.google.firebase:firebase-messaging:24.0.3")
    //zalopay
    implementation("com.squareup.okhttp3:okhttp:4.6.0")
    implementation("commons-codec:commons-codec:1.14")
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}