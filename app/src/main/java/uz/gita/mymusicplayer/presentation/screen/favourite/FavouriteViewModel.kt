package uz.gita.mymusicplayer.presentation.screen.favourite

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
import uz.gita.mymusicplayer.domain.repository.AppRepository
import uz.gita.mymusicplayer.utils.MyEventBus
import uz.gita.mymusicplayer.utils.checkMusics
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: AppRepository,
    private val directions: FavouriteContact.Directions
) : ViewModel(), FavouriteContact.ViewModel {
    override val container =
        container<FavouriteContact.UiState, FavouriteContact.SideEffect>(FavouriteContact.UiState.Loading)


    override fun onEventDispatcher(intent: FavouriteContact.Intent) {
        when (intent) {
            FavouriteContact.Intent.CheckMusicExistance -> {
                viewModelScope.launch {
                    MyEventBus.roomCursor = repository.getFavouriteMusics()

                    checkMusics(this@FavouriteViewModel::onEventDispatcher)
                    intent { reduce { FavouriteContact.UiState.IsExistMusic } }
                }
            }

            FavouriteContact.Intent.BackToList -> {
                viewModelScope.launch {
                    directions.backToHome()
                }
            }

            is FavouriteContact.Intent.LoadMusic -> {
                repository.getAllMusics().onEach {
                    intent { reduce { FavouriteContact.UiState.PreparedData } }
                }.launchIn(viewModelScope)
            }

            FavouriteContact.Intent.OpenPlayScreen -> {
                viewModelScope.launch {
                    directions.openPlayScreen()
                }
            }

            is FavouriteContact.Intent.UserCommand -> {
                intent { postSideEffect(FavouriteContact.SideEffect.StartMusicService(intent.commandEnum)) }
            }

            is FavouriteContact.Intent.DeleteMusic -> {
                repository.removeFromFavourite(intent.musicData.toEntity())
            }

        }
    }
}