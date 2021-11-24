package pl.osmalek.bartek.composeinject.app

import dagger.Component
import javax.inject.Singleton
import pl.osmalek.bartek.composeinject.ComposeInjector

@Component(modules = [ComposeModule::class])
@Singleton
interface TestComponent {
  fun injector(): ComposeInjector
}
