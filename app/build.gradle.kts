plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.roywatson.garage.rhawreadium240test"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.roywatson.garage.rhawreadium240test"
        minSdk = 22
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("androidx.navigation:navigation-compose:2.7.5")

    implementation("io.insert-koin:koin-android:3.4.0")
    implementation("io.insert-koin:koin-androidx-compose:3.4.4")
    implementation("io.insert-koin:koin-androidx-navigation:3.4.0")
    testImplementation("io.insert-koin:koin-test:3.4.0")
    testImplementation("io.insert-koin:koin-test-junit4:3.4.0")

    implementation("org.readium.kotlin-toolkit:readium-shared:2.4.0")
    implementation("org.readium.kotlin-toolkit:readium-streamer:2.4.0")
    implementation("org.readium.kotlin-toolkit:readium-navigator:2.4.0")
    implementation("org.readium.kotlin-toolkit:readium-navigator-media2:2.4.0")
    implementation("org.readium.kotlin-toolkit:readium-opds:2.4.0")
    implementation("org.readium.kotlin-toolkit:readium-lcp:2.4.0")

    implementation("androidx.lifecycle:lifecycle-common:2.6.2")
    implementation("androidx.lifecycle:lifecycle-service:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
//    implementation("androidx.lifecycle:lifecycle-common:2.5.1") {
//        version {strictly("2.5.1") }
//    }
//    implementation("androidx.lifecycle:lifecycle-service:2.5.1") {
//        version {strictly("2.5.1") }
//    }
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1") {
//        version {strictly("2.5.1") }
//    }

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}