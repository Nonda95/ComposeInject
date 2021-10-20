object Sdk {
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 31
    const val COMPILE_SDK_VERSION = 31
}

object Versions {
    const val ANDROIDX_TEST_EXT = "1.1.3"
    const val ANDROIDX_TEST = "1.4.0"
    const val APPCOMPAT = "1.3.0"
    const val CONSTRAINT_LAYOUT = "2.0.4"
    const val CORE_KTX = "1.6.0"
    const val ESPRESSO_CORE = "3.4.0"
    const val JUNIT = "4.13.2"
    const val KTLINT = "0.42.0"
}

object Activity {
    const val Version = "1.3.1"
    const val Core = "androidx.appcompat:appcompat:$Version"
    const val Compose = "androidx.activity:activity-compose:$Version"
}

object Dagger {
    const val Version = "2.39.1"
    const val Core = "com.google.dagger:dagger:$Version"
    const val Compiler = "com.google.dagger:dagger-compiler:$Version"
}

object Compose {
    const val Version = "1.0.4"
    const val Runtime = "androidx.compose.runtime:runtime:$Version"
    const val Material = "androidx.compose.material:material:$Version"
}

object JavaX {
    const val Inject = "javax.inject:javax.inject:1"
}

object AutoService {
    const val Core = "com.google.auto.service:auto-service:1.0"
    const val Processor = "com.google.auto.service:auto-service:1.0"
}

object JavaPoet {
    const val  Core = "com.squareup:javapoet:1.13.0"
}

object BuildPluginsVersion {
    const val DETEKT = "1.17.1"
    const val KTLINT = "10.1.0"
    const val VERSIONS_PLUGIN = "0.39.0"
}

object TestingLib {
    const val Junit = "junit:junit:${Versions.JUNIT}"
}

object AndroidTestingLib {
    const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT}"
    const val ANDROIDX_TEST_EXT_JUNIT_KTX = "androidx.test.ext:junit-ktx:${Versions.ANDROIDX_TEST_EXT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}
