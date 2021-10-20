package pl.osmalek.bartek.composeinject

import dagger.Module

@Module
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ComposeInjectModule
