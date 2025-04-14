plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.test2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.test2"
        minSdk = 21
        targetSdk = 34
        versionCode = 3
        versionName = "2.1"
        multiDexEnabled = true
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage:20.1.0")
    implementation("com.google.firebase:firebase-firestore:24.4.4")

    // AndroidX dependencies
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("androidx.activity:activity:1.9.2")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    
    // Removing explicit version of play-services-measurement-api (handled by firebase-bom)
    // implementation("com.google.android.gms:play-services-measurement-api:21.2.0")
    
    // Multidex dependency
    implementation("androidx.multidex:multidex:2.0.1")
    
    // Google Play Services (aligned versions to avoid conflicts)
    implementation("com.google.android.gms:play-services-measurement-api:21.2.0")
    implementation("com.google.android.gms:play-services-measurement-base:22.1.2")
    implementation("com.google.android.gms:play-services-measurement-sdk-api:22.1.2")
    implementation("com.google.android.gms:play-services-measurement:22.1.2")
    implementation("com.google.android.gms:play-services-measurement-impl:22.1.2")
}

// Force resolution of specific versions to avoid dependency conflicts
//configurations.all {
//    resolutionStrategy {
//        eachDependency {
//            if (requested.group == "com.google.android.gms" && requested.name == "play-services-measurement-api") {
 //               useVersion("21.2.0")
//            }
 //           if (requested.group == "com.google.android.gms" && requested.name.startsWith("play-services-measurement")) {
 //               useVersion("22.1.2")
 //           }
 //       }
 //   }
//}