package pl.osmalek.bartek.composeinject.app

import dagger.Module
import pl.osmalek.bartek.composeinject.ComposeInjectModule

@ComposeInjectModule
@Module(includes = [ComposeInject_ComposeModule::class])
object ComposeModule
