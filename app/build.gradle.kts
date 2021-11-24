plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
}

android {
  compileSdk = Sdk.COMPILE_SDK_VERSION

  defaultConfig {
    minSdk = Sdk.MIN_SDK_VERSION
    targetSdk = Sdk.TARGET_SDK_VERSION

    applicationId = AppCoordinates.APP_ID
    versionCode = AppCoordinates.APP_VERSION_CODE
    versionName = AppCoordinates.APP_VERSION_NAME
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildFeatures {
    viewBinding = true
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = Compose.Version
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  lint {
    isWarningsAsErrors = true
    isAbortOnError = true
  }
}

dependencies {
  implementation(kotlin("stdlib-jdk7"))
  implementation(project(":compose"))

  implementation(project(":annotation"))
  kapt(project(":processor"))

  implementation(Dagger.Core)
  kapt(Dagger.Compiler)
  implementation(Compose.Runtime)
  implementation(Compose.Material)
  implementation(Activity.Core)
  implementation(Activity.Compose)

  testImplementation(TestingLib.Junit)

  androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
  androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT_KTX)
  androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_RULES)
  androidTestImplementation(AndroidTestingLib.ESPRESSO_CORE)
}
