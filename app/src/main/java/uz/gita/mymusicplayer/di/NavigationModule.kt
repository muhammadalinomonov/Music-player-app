package uz.gita.mymusicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.mymusicplayer.navigation.AppNavigator
import uz.gita.mymusicplayer.navigation.NavigationDispatcher
import uz.gita.mymusicplayer.navigation.NavigationHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @[Binds Singleton]
    fun bindAppNavigator(impl: NavigationDispatcher): AppNavigator

    @[Binds Singleton]
    fun bindNavigatorHandler(impl: NavigationDispatcher): NavigationHandler

}