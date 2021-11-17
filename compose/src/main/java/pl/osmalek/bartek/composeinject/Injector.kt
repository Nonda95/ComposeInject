@file:Suppress("unused")

package pl.osmalek.bartek.composeinject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import javax.inject.Provider

interface Injector {
    fun <T : Any> get(clazz: Class<T>): T = provider(clazz).get()
    fun <T : Any> provider(clazz: Class<T>): Provider<T>
}

@Composable
inline fun <reified T : Any> rememberInject(vararg keys: Any?): T {
    val injector = LocalInjector.current
    return remember(keys) { injector.get(T::class.java) }
}

@Composable
inline fun <reified T : Any> rememberInject(): T {
    val injector = LocalInjector.current
    return remember { injector.get(T::class.java) }
}

@Composable
inline fun <reified T : Any> rememberInject(key1: Any?): T {
    val injector = LocalInjector.current
    return remember(key1) { injector.get(T::class.java) }
}

@Composable
inline fun <reified T : Any> inject(): T {
    val injector = LocalInjector.current
    return injector.get(T::class.java)
}

@Composable
inline fun <reified T : Any> provider(): Provider<T> {
    val injector = LocalInjector.current
    return injector.provider(T::class.java)
}

@Composable
fun ComposeInject(injector: Injector, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalInjector provides injector, content = content)
}

val LocalInjector = staticCompositionLocalOf<Injector> {
    error("No Injector was provided via LocalInjector")
}
