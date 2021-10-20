package pl.osmalek.bartek.composeinject

import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ComposeInjector @Inject constructor(
    @ComposeInjectMap private val providers: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>
) : Injector {
    override fun <T: Any> get(clazz: Class<T>): T {
        return providers.getValue(clazz).get() as T
    }
}
