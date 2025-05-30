plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
// custom
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.openapiGenerator)
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "ru.kotlix.fitfoodie"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.kotlix.fitfoodie"
        minSdk = 29
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }

    sourceSets {
        getByName("main") {
            kotlin.srcDirs("$projectDir/build/generated/src/main/kotlin")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // retrofit
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging)
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.retrofit2.converter.moshi)
    implementation(libs.retrofit2.converter.scalars)
}

kapt {
    correctErrorTypes = true
}

openApiGenerate {
    generatorName = "kotlin"
    library = "jvm-retrofit2"
    inputSpec = "$projectDir/openapi/recipe.yaml"
    outputDir = "$projectDir/build/generated"

    apiPackage = "ru.kotlix.fitfoodie.api"
    modelPackage = "ru.kotlix.fitfoodie.api.dto"
    invokerPackage = "ru.kotlix.fitfoodie.api.invoker"

    configOptions.apply {
        put("serializationLibrary", "moshi")
        put("useCoroutines", "true")

        put("parameterAnnotations", "")
        put("sourceFolder", "src/main/kotlin")
    }
}

tasks.named("preBuild") {
    dependsOn(tasks.named("openApiGenerate"))
}