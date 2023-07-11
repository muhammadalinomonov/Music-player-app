package uz.gita.mymusicplayer.presentation.screen.favourite

import org.orbitmvi.orbit.ContainerHost
import uz.gita.mymusicplayer.data.model.CommandEnum
import uz.gita.mymusicplayer.data.model.MusicData

interface FavouriteContact {
    sealed interface Intent {
        object CheckMusicExistance : Intent
        object LoadMusic : Intent
        object OpenPlayScreen : Intent
        data class UserCommand(val commandEnum: CommandEnum) : Intent
        object BackToList : Intent
        data class DeleteMusic(val musicData: MusicData) : Intent
    }

    sealed interface UiState {
        object Loading : UiState
        object IsExistMusic : UiState
        object PreparedData : UiState
    }

    sealed interface SideEffect {
        data class StartMusicService(val commandEnum: CommandEnum) : SideEffect
    }

    interface ViewModel : ContainerHost<UiState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    interface Directions {
        suspend fun backToHome()
        suspend fun openPlayScreen()
    }
}
