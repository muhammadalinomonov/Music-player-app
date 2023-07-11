package uz.gita.mymusicplayer.presentation.screen.favourite

import uz.gita.mymusicplayer.navigation.AppNavigator
import uz.gita.mymusicplayer.presentation.screen.play.PlayScreen
import javax.inject.Inject

class FavouriteDirections @Inject constructor(private val navigator: AppNavigator) :
    FavouriteContact.Directions {
    override suspend fun backToHome() {
        navigator.pop()
    }

    override suspend fun openPlayScreen() {
        navigator.navigateTo(PlayScreen())
    }
}