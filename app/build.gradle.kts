plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.copyofassignment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.copyofassignment"
        minSdk = 31
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.github.kal72:RackMonthPicker:1.6.1")
    implementation ("com.github.voghDev:PdfViewPager:1.1.1")
    implementation ("com.itextpdf:itext7-core:7.2.3")
    implementation ("org.slf4j:slf4j-simple:2.0.9")
    implementation ("androidx.work:work-runtime:2.8.1")
    implementation ("net.zetetic:android-database-sqlcipher:4.5.3")
    implementation ("androidx.sqlite:sqlite:2.1.0")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}