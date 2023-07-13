package uz.gita.mymusicplayer.presentation.screen.musiclist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.mymusicplayer.utils.MyEventBus
import uz.gita.mymusicplayer.utils.getMusicCursor
import javax.inject.Inject

@HiltViewModel
class MusicListViewModel @Inject constructor(private val direction: MusicListContract.Directions) :
    MusicListContract.ViewModel, ViewModel() {
    override val container =
        container<MusicListContract.UiState, MusicListContract.SideEffect>(MusicListContract.UiState.Loading)

    init {
        intent {
            reduce {
                if (MyEventBus.storageCursor != null) MusicListContract.UiState.PreparedData
                else MusicListContract.UiState.Loading
            }
        }
    }

    override fun onEventDispatcher(intent: MusicListContract.Intent) {
        when (intent) {
            is MusicListContract.Intent.LoadMusic -> {
                intent.context.getMusicCursor().onEach {
                    MyEventBus.storageCursor = it
                    intent {
                        reduce {
                            MusicListContract.UiState.PreparedData
                        }
                    }
                }.launchIn(viewModelScope)
            }

            MusicListContract.Intent.OpenPlayScreen -> {
                viewModelScope.launch {
                    direction.openPlayScreen()
                }
            }

            is MusicListContract.Intent.UserCommand -> {
                intent {
                    postSideEffect(MusicListContract.SideEffect.StartMusicService(intent.commandEnum))
                }
            }

            MusicListContract.Intent.RequestPermission -> {
                intent { postSideEffect(MusicListContract.SideEffect.OpenPermissionDialog) }
            }

            MusicListContract.Intent.OpenFavouriteScreen -> {
                viewModelScope.launch {
                    direction.openFavouriteScreen()
                }
            }

            MusicListContract.Intent.PlayMusic -> {
                intent { postSideEffect(MusicListContract.SideEffect.PlayMusicService) }
            }
        }
    }
}