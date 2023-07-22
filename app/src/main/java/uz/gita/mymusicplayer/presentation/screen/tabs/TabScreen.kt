package uz.gita.mymusicplayer.presentation.screen.tabs

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.mymusicplayer.data.model.CommandEnum
import uz.gita.mymusicplayer.data.model.CursorEnum
import uz.gita.mymusicplayer.navigation.AppScreen
import uz.gita.mymusicplayer.presentation.screen.favourite.FavouriteContact
import uz.gita.mymusicplayer.presentation.screen.favourite.FavouriteScreen
import uz.gita.mymusicplayer.presentation.screen.favourite.FavouriteViewModel
import uz.gita.mymusicplayer.presentation.screen.musiclist.MusicListContract
import uz.gita.mymusicplayer.presentation.screen.musiclist.MusicListScreen
import uz.gita.mymusicplayer.presentation.screen.musiclist.MusicListViewModel
import uz.gita.mymusicplayer.utils.MyEventBus
import uz.gita.mymusicplayer.utils.checkPermission
import uz.gita.mymusicplayer.utils.startMusicService

class TabScreen : AppScreen() {


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        @OptIn(ExperimentalFoundationApi::class)
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            TabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF03A9F4)),
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color(0xFF03A9F4)
            ) {
                tabs.forEachIndexed { index, item ->
                    Tab(
                        selected = index == pagerState.currentPage,
                        text = { Text(text = item.title, color = Color.White, fontSize = 22.sp) },
                        icon = { },
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    )
                }
            }

            HorizontalPager(
                pageCount = tabs.size,
                state = pagerState,
            ) {
                when (it) {
                    0 -> {
                        val context = LocalContext.current
                        val viewModel: MusicListContract.ViewModel =
                            getViewModel<MusicListViewModel>()

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
                                            viewModel.onEventDispatcher(
                                                MusicListContract.Intent.LoadMusic(
                                                    context
                                                )
                                            )
                                        }
                                    } else {
                                        context.checkPermission(
                                            arrayListOf(
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                            )
                                        ) {
                                            viewModel.onEventDispatcher(
                                                MusicListContract.Intent.LoadMusic(
                                                    context
                                                )
                                            )
                                        }
                                    }
                                }

                                is MusicListContract.SideEffect.StartMusicService -> {
                                    MyEventBus.currentCursorEnum = CursorEnum.STORAGE

                                    try {

                                        startMusicService(context, sideEffect.commandEnum)
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            e.message ?: "This operation is impossible",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                MusicListContract.SideEffect.PlayMusicService -> {
                                    MyEventBus.currentCursorEnum = CursorEnum.STORAGE
                                    startMusicService(context, CommandEnum.PLAY)
                                }
                            }
                        }


                        MusicListScreen().MusicListContent(
                            uiState = uiState,
                            onEventDispatcher = viewModel::onEventDispatcher
                        )
                    }

                    1 -> {
                        val viewModel: FavouriteContact.ViewModel =
                            getViewModel<FavouriteViewModel>()

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

                        FavouriteScreen().FavouriteScreenContent(
                            uiState,
                            viewModel::onEventDispatcher
                        )
                    }
                }
//                tabs[pagerState.currentPage].screen.Content()
            }
        }
    }
}