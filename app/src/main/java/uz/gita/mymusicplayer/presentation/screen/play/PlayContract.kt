package uz.gita.mymusicplayer.presentation.screen.play

import org.orbitmvi.orbit.ContainerHost
import uz.gita.mymusicplayer.data.model.CommandEnum
import uz.gita.mymusicplayer.data.model.MusicData

interface PlayContract {
    interface ViewModel : ContainerHost<UiState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    sealed interface UiState {
        object InitState : UiState
        data class CheckMusic(val isSaved: Boolean) : UiState
    }

    sealed interface SideEffect {
        data class UserAction(val actionEnum: CommandEnum) : SideEffect
    }

    sealed interface Intent {
        data class UserAction(val actionEnum: CommandEnum) : Intent
        data class SaveMusic(val musicData: MusicData) : Intent
        data class DeleteMusic(val musicData: MusicData) : Intent
        data class CheckMusic(val musicData: MusicData) : Intent
        data class IsRepeated(val isRepeated:Boolean):Intent
        object Back : Intent
    }

    interface Directions {
        suspend fun back()
    }
}