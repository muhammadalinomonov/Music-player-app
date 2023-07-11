package uz.gita.mymusicplayer.presentation.screen.musiclist

import uz.gita.mymusicplayer.navigation.AppNavigator
import uz.gita.mymusicplayer.presentation.screen.favourite.FavouriteScreen
import uz.gita.mymusicplayer.presentation.screen.play.PlayScreen
import javax.inject.Inject

class MusicListDirectionImpl @Inject constructor(private val navigator: AppNavigator) :
    MusicListContract.Directions {
    override suspend fun openPlayScreen() {
        navigator.navigateTo(PlayScreen())
    }

    override suspend fun openFavouriteScreen() {
        navigator.navigateTo(FavouriteScreen())
    }
}