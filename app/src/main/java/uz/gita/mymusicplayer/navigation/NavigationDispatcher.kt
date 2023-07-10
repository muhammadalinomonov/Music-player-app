package uz.gita.mymusicplayer.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationDispatcher @Inject constructor() : AppNavigator, NavigationHandler {
    override val navigatorState = MutableSharedFlow<NavigationArgs>()

    override suspend fun navigateTo(screen: AppScreen) = navigate {
        push(screen)
    }

    override suspend fun replace(screen: AppScreen) = navigate {
        replace(screen)
    }

    override suspend fun pop() = navigate {
        pop()
    }

    private suspend fun navigate(args: NavigationArgs) {
        navigatorState.emit(args)
    }
}