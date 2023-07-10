package uz.gita.mymusicplayer.presentation.screen.play

import uz.gita.mymusicplayer.navigation.AppNavigator
import javax.inject.Inject

class PlayDirectionImpl @Inject constructor(private val navigator: AppNavigator) :
    PlayContract.Directions {
    override suspend fun back() {
        navigator.pop()
    }
}