package pl.osmalek.bartek.composeinject.app

import dagger.Component
import javax.inject.Singleton
import pl.osmalek.bartek.composeinject.ComposeInjector
import pl.osmalek.bartek.composeinject.Injector

@Component(modules = [ComposeModule::class])
@Singleton
interface TestComponent {
    fun injector(): ComposeInjector
}
