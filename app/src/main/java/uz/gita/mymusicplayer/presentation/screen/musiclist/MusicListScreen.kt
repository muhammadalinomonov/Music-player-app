package uz.gita.mymusicplayer.presentation.screen.musiclist

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.mymusicplayer.R
import uz.gita.mymusicplayer.data.model.CommandEnum
import uz.gita.mymusicplayer.data.model.CursorEnum
import uz.gita.mymusicplayer.navigation.AppScreen
import uz.gita.mymusicplayer.ui.component.CurrentMusicItemComponent
import uz.gita.mymusicplayer.ui.component.LoadingComponent
import uz.gita.mymusicplayer.ui.component.MusicItem
import uz.gita.mymusicplayer.utils.MyEventBus
import uz.gita.mymusicplayer.utils.checkPermission
import uz.gita.mymusicplayer.utils.getMusicDataByPosition
import uz.gita.mymusicplayer.utils.startMusicService

class MusicListScreen : AppScreen(), Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "All tracks"

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                )
            }
        }

    @Composable
    override fun Content() {

        Log.d("TTTT", "musicListscreen content")
        val context = LocalContext.current
        val viewModel: MusicListContract.ViewModel = getViewModel<MusicListViewModel>()

        val uiState = viewModel.collectAsState()
        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                MusicListContract.SideEffect.OpenPermissionDialog -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.checkPermission(
                            arrayListOf(
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
                    MyEventBus.currentCursorEnum = CursorEnum.STORAGE
                    startMusicService(context, sideEffect.commandEnum)
                }

                MusicListContract.SideEffect.PlayMusicService -> {
                    MyEventBus.currentCursorEnum = CursorEnum.STORAGE
                    startMusicService(context, CommandEnum.PLAY)
                }
            }
        }


        MusicListContent(uiState, viewModel::onEventDispatcher)
    }

    @Composable
    fun MusicListContent(
        uiState: State<MusicListContract.UiState>,
        onEventDispatcher: (MusicListContract.Intent) -> Unit
    ) {


        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                /*Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(color = Color(0xFF03A9F4))
                        .padding(start = 8.dp)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Music Player",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.heart3),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 4.dp)
                            .clickable { onEventDispatcher(MusicListContract.Intent.OpenFavouriteScreen) })
                }*/

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
                            for (i in 0 until MyEventBus.storageCursor!!.count) {
                                item {
                                    MusicItem(
                                        musicData = MyEventBus.storageCursor!!.getMusicDataByPosition(
                                            i
                                        )
                                    ) {
                                        MyEventBus.storagePos = i
                                        onEventDispatcher.invoke(MusicListContract.Intent.PlayMusic)
                                        onEventDispatcher.invoke(MusicListContract.Intent.OpenPlayScreen)
                                    }
                                }
                            }
                        }

                        //todo
                        if (MyEventBus.storagePos != -1 ||
                            MyEventBus.storagePos >= MyEventBus.storageCursor!!.count
                        ) {
//                             {
                            CurrentMusicItemComponent(
                                modifier = Modifier/*.align(Alignment.BottomCenter)*/,
//                                musicData = MyEventBus.storageCursor!!.getMusicDataByPosition(MyEventBus.storagePos),
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
