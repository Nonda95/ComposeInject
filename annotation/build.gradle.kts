version = LibraryCoordinates.LIBRARY_VERSION

plugins {
    id("java-library")
    kotlin("jvm")
    id("maven-publish")
    publish
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    api(JavaX.Inject)
    implementation(Dagger.Core)

    testImplementation(TestingLib.Junit)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
