version = LibraryCoordinates.LIBRARY_VERSION

plugins {
  id("java-library")
  kotlin("jvm")
  kotlin("kapt")
  id("maven-publish")
  publish
}

kapt.includeCompileClasspath = false

dependencies {
  implementation(kotlin("stdlib-jdk7"))
  implementation(project(":annotation"))

  implementation(Dagger.Core)
  compileOnly(AutoService.Core)
  kapt(AutoService.Processor)

  implementation(JavaPoet.Core)

  testImplementation(TestingLib.Junit)
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}
