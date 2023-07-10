package uz.gita.mymusicplayer.presentation.screen.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val directions: PlayContract.Directions
) : PlayContract.ViewModel, ViewModel() {
    override val container =
        container<PlayContract.UiState, PlayContract.SideEffect>(PlayContract.UiState.InitState)

    override fun onEventDispatcher(intent: PlayContract.Intent) {
        when (intent) {
            PlayContract.Intent.Back -> {
                viewModelScope.launch {
                    directions.back()
                }
            }

            is PlayContract.Intent.CheckMusic -> {
                //todo
            }

            is PlayContract.Intent.UserAction -> {
                intent {
                    postSideEffect(PlayContract.SideEffect.UserAction(intent.actionEnum))
                }
            }

            is PlayContract.Intent.DeleteMusic -> {
                //todo
            }

            is PlayContract.Intent.SaveMusic -> {
                //todo
            }
        }
    }
}