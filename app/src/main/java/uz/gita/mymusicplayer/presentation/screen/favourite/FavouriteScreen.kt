package uz.gita.mymusicplayer.presentation.screen.favourite

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.mymusicplayer.R
import uz.gita.mymusicplayer.data.model.CommandEnum
import uz.gita.mymusicplayer.data.model.CursorEnum
import uz.gita.mymusicplayer.navigation.AppScreen
import uz.gita.mymusicplayer.ui.component.LoadingComponent
import uz.gita.mymusicplayer.ui.component.MusicItem
import uz.gita.mymusicplayer.utils.MyEventBus
import uz.gita.mymusicplayer.utils.getMusicDataByPosition
import uz.gita.mymusicplayer.utils.startMusicService

class FavouriteScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel: FavouriteContact.ViewModel = getViewModel<FavouriteViewModel>()

        val uiState = viewModel.collectAsState()

        val context = LocalContext.current
        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is FavouriteContact.SideEffect.StartMusicService -> {
                    MyEventBus.currentCursorEnum = CursorEnum.SAVED
                    startMusicService(context, sideEffect.commandEnum)
                }
            }
        }
        viewModel.onEventDispatcher(FavouriteContact.Intent.CheckMusicExistance)

        FavouriteScreenContent(uiState, viewModel::onEventDispatcher)
    }

    @Composable
    private fun FavouriteScreenContent(
        uiState: State<FavouriteContact.UiState>,
        onEventDispatcher: (FavouriteContact.Intent) -> Unit
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState.value) {

                FavouriteContact.UiState.IsExistMusic -> {
                    LoadingComponent()
                    onEventDispatcher.invoke(FavouriteContact.Intent.LoadMusic)
                }

                FavouriteContact.UiState.Loading -> {
                    onEventDispatcher.invoke(FavouriteContact.Intent.CheckMusicExistance)
                }

                is FavouriteContact.UiState.PreparedData -> {

                    Log.d("TTT", "count -> ${MyEventBus.roomCursor!!.count}")
                    if (MyEventBus.roomCursor!!.count == 0) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(180.dp),
                            painter = painterResource(id = R.drawable.ic_music),
                            contentDescription = null
                        )
                    } else {
                        LazyColumn {
                            for (pos in 0 until MyEventBus.roomCursor!!.count) {
                                item {
                                    MusicItem(
                                        musicData = MyEventBus.roomCursor!!.getMusicDataByPosition(
                                            pos
                                        ),
                                        onClick = {
                                            //MyEventBus.selectMusicPos = pos
                                            MyEventBus.roomPos = pos
                                            onEventDispatcher.invoke(
                                                FavouriteContact.Intent.UserCommand(
                                                    CommandEnum.PLAY
                                                )
                                            )
                                            onEventDispatcher.invoke(FavouriteContact.Intent.OpenPlayScreen)
                                        },
//                                        onLongClick = {}
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}