plugins {
  `kotlin-dsl`
}
repositories {
  google()
  mavenCentral()
}

object Plugins {
  const val AGP = "7.0.4"
  const val DOKKA = "1.6.0"
  const val KOTLIN = "1.6.10"
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Plugins.KOTLIN}")
  implementation("com.android.tools.build:gradle:7.0.3")
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:${Plugins.DOKKA}")
  implementation("org.jetbrains.dokka:dokka-core:${Plugins.DOKKA}")
}
