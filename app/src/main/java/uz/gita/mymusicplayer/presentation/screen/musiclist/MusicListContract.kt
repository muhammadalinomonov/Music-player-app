package uz.gita.mymusicplayer.presentation.screen.musiclist

import android.content.Context
import org.orbitmvi.orbit.ContainerHost
import uz.gita.mymusicplayer.data.model.CommandEnum

interface MusicListContract {

    sealed interface Intent {
        object RequestPermission : Intent
        data class LoadMusic(val context: Context) : Intent

        object PlayMusic:Intent
        object OpenPlayScreen : Intent
        data class UserCommand(val commandEnum: CommandEnum) : Intent
        object OpenFavouriteScreen : Intent
    }

    sealed interface SideEffect {
        data class StartMusicService(val commandEnum: CommandEnum) : SideEffect
        object OpenPermissionDialog : SideEffect
        object PlayMusicService : SideEffect
    }

    sealed interface UiState {
        object Loading : UiState
        object PreparedData : UiState
    }

    interface ViewModel : ContainerHost<UiState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    interface Directions {
        suspend fun openPlayScreen()
        suspend fun openFavouriteScreen()
    }
}