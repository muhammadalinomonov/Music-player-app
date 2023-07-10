package uz.gita.mymusicplayer.presentation.screen.musiclist

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.mymusicplayer.data.model.CommandEnum
import uz.gita.mymusicplayer.navigation.AppScreen
import uz.gita.mymusicplayer.ui.component.CurrentMusicItemComponent
import uz.gita.mymusicplayer.ui.component.LoadingComponent
import uz.gita.mymusicplayer.ui.component.MusicItem
import uz.gita.mymusicplayer.utils.MyEventBus
import uz.gita.mymusicplayer.utils.checkPermission
import uz.gita.mymusicplayer.utils.getMusicDataByPosition
import uz.gita.mymusicplayer.utils.startMusicService

class MusicListScreen : AppScreen() {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: MusicListContract.ViewModel = getViewModel<MusicListViewModel>()

        val uiState = viewModel.collectAsState()
        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                MusicListContract.SideEffect.OpenPermissionDialog -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.checkPermission(
                            arrayListOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.POST_NOTIFICATIONS,
                                Manifest.permission.READ_MEDIA_AUDIO
                            )
                        ) {
                            viewModel.onEventDispatcher(MusicListContract.Intent.LoadMusic(context))
                        }
                    } else {
                        context.checkPermission(
                            arrayListOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            viewModel.onEventDispatcher(MusicListContract.Intent.LoadMusic(context))
                        }
                    }
                }

                is MusicListContract.SideEffect.StartMusicService -> {
                    startMusicService(context, sideEffect.commandEnum)
                }
            }
        }


        MusicListContent(uiState, viewModel::onEventDispatcher)
    }

    @Composable
    private fun MusicListContent(
        uiState: State<MusicListContract.UiState>,
        onEventDispatcher: (MusicListContract.Intent) -> Unit
    ) {


        val context = LocalContext.current

        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = "Music Player", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                }

                when (uiState.value) {
                    MusicListContract.UiState.Loading -> {
                        LoadingComponent()
                        onEventDispatcher(MusicListContract.Intent.RequestPermission)
                    }

                    MusicListContract.UiState.PreparedData -> {
                        LazyColumn(
                            modifier = Modifier
                                .height(0.dp)
                                .weight(1f)
                        ) {
                            for (i in 0 until MyEventBus.cursor!!.count) {
                                item {
                                    MusicItem(
                                        musicData = MyEventBus.cursor!!.getMusicDataByPosition(i)
                                    ) {
                                        MyEventBus.selectMusicPos = i
                                        onEventDispatcher(MusicListContract.Intent.OpenPlayScreen)
                                        onEventDispatcher(
                                            MusicListContract.Intent.UserCommand(
                                                CommandEnum.PLAY
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        //todo
                        if (MyEventBus.selectMusicPos != -1) {
//                             {
                                CurrentMusicItemComponent(
                                    modifier = Modifier/*.align(Alignment.BottomCenter)*/,
//                                musicData = MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.selectMusicPos),
                                    onClick = { onEventDispatcher(MusicListContract.Intent.OpenPlayScreen) },
                                    onEventDispatcher
                                )
//                            }
                        }
                    }
                }
            }
        }
    }
}
