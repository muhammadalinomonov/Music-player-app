package uz.gita.mymusicplayer.presentation.screen.play

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.mymusicplayer.R
import uz.gita.mymusicplayer.data.model.CommandEnum
import uz.gita.mymusicplayer.data.model.CursorEnum
import uz.gita.mymusicplayer.navigation.AppScreen
import uz.gita.mymusicplayer.ui.theme.leftColor
import uz.gita.mymusicplayer.ui.theme.rightColor
import uz.gita.mymusicplayer.utils.MyEventBus
import uz.gita.mymusicplayer.utils.getMusicDataByPosition
import uz.gita.mymusicplayer.utils.getTime
import uz.gita.mymusicplayer.utils.startMusicService
import java.util.concurrent.TimeUnit


class PlayScreen : AppScreen() {

    @Composable
    override fun Content() {
        val viewModel: PlayContract.ViewModel = getViewModel<PlayViewModel>()

        val context = LocalContext.current

        val uiState = viewModel.collectAsState()


        PlayScreenContent(uiState, viewModel::onEventDispatcher)
        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is PlayContract.SideEffect.UserAction -> {
                    startMusicService(context, sideEffect.actionEnum)
                }
            }
        }
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun PlayScreenContent(
        uiState: State<PlayContract.UiState>,
        onEventDispatcher: (PlayContract.Intent) -> Unit
    ) {

        val musicData = MyEventBus.currentMusicData.collectAsState(
            initial = if (MyEventBus.currentCursorEnum == CursorEnum.SAVED)
                MyEventBus.roomCursor!!.getMusicDataByPosition(MyEventBus.roomPos)
            else MyEventBus.storageCursor!!.getMusicDataByPosition(MyEventBus.storagePos)
        )

        onEventDispatcher(PlayContract.Intent.CheckMusic(musicData.value!!))

        val seekBarState = MyEventBus.currentTimeFlow.collectAsState(initial = 0)
        var seekBarValue by remember { mutableStateOf(seekBarState.value) }
        val musicIsPlaying = MyEventBus.isPlaying.collectAsState()


        val isRepeated = MyEventBus.isRepeatedFlow.collectAsState()
        val milliseconds = musicData.value!!.duration
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        val minutes = (milliseconds / 1000 / 60) % 60
        val seconds = (milliseconds / 1000) % 60

        val duration = if (hours == 0L) "%02d:%02d".format(minutes, seconds)
        else "%02d:%02d:%02d".format(hours, minutes, seconds) // 03:45

        var isSaved by remember { mutableStateOf(false) }


        var currentRotation by remember { mutableStateOf(0f) }

        val rotation = remember { Animatable(currentRotation) }


        LaunchedEffect(MyEventBus.isPlaying.value) {
            if (MyEventBus.isPlaying.value) {
                // Infinite repeatable rotation when is playing
                rotation.animateTo(
                    targetValue = currentRotation + 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(10000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                ) {
                    currentRotation = value
                }
            } else {
                if (currentRotation > 0f) {
                    // Slow down rotation on pause
                    rotation.animateTo(
                        targetValue = currentRotation + 50,
                        animationSpec = tween(
                            durationMillis = 2800,
                            easing = LinearOutSlowInEasing
                        )
                    ) {
                        currentRotation = value
                    }
                }
            }
        }
//        Vinyl(modifier = modifier.padding(24.dp), rotationDegrees = rotation.value)


        when (uiState.value) {
            is PlayContract.UiState.CheckMusic -> {
                isSaved = (uiState.value as PlayContract.UiState.CheckMusic).isSaved
            }

            PlayContract.UiState.InitState -> {

            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            leftColor,
                            rightColor,
                        )
                    ),
                )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(35.dp)
                        .clip(CircleShape)
                        .clickable {
                            onEventDispatcher(PlayContract.Intent.Back)
                        }
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(0.dp)
                    .weight(1.7f)
            ) {

                if (musicData.value!!.albumArt != null)
                    Image(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .size(250.dp)

                            .rotate(rotation.value)
                            .align(Alignment.CenterHorizontally),
                        bitmap = musicData.value!!.albumArt!!.asImageBitmap(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                else {


                    Image(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .size(250.dp)
                            .rotate(rotation.value)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = R.drawable.music_disk),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }


                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    text = musicData.value!!.title ?: "Unknown",
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = musicData.value!!.artist ?: "-- -- --",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.dp)
                    .weight(1f)
            ) {
                Slider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    value = seekBarState.value.toFloat(),
                    onValueChange = { newState ->
                        seekBarValue = newState.toInt()
                        onEventDispatcher.invoke(PlayContract.Intent.UserAction(CommandEnum.UPDATE_SEEKBAR))
                    },
                    onValueChangeFinished = {
                        MyEventBus.currentTime.value = seekBarValue
                        onEventDispatcher.invoke(PlayContract.Intent.UserAction(CommandEnum.UPDATE_SEEKBAR))
                    },
                    valueRange = 0f..musicData.value!!.duration.toFloat(),
                    steps = 1000,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFa8dadc),
                        activeTickColor = Color(0xFFFFFFFF),
                        activeTrackColor = Color(0xFFCCC2C2),
                        inactiveTickColor = Color.Gray,
                        inactiveTrackColor = Color.Transparent

                    )
                )

                // 00:00
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .width(0.dp)
                            .weight(1f),
                        text = getTime(seekBarState.value / 1000)
                    )
                    // 03:45
                    Text(
                        modifier = Modifier
                            .width(0.dp)
                            .weight(1f),
                        textAlign = TextAlign.End,
                        text = duration
                    )
                }


                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    Box(modifier = Modifier.size(50.dp)) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .padding(4.dp)
                                .clickable {
                                    onEventDispatcher(PlayContract.Intent.IsRepeated(MyEventBus.isRepeated))
                                },
                            painter = painterResource(id = R.drawable.ic_repeat),
                            contentDescription = null
                        )

                        if (isRepeated.value) {
                            Text(
                                text = "1",
                                Modifier
                                    .size(26.dp)
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                            .clickable {
                                onEventDispatcher.invoke(PlayContract.Intent.UserAction(CommandEnum.PREV))
                                seekBarValue = 0
                            },
                        painter = painterResource(id = R.drawable.previous),
                        contentDescription = null
                    )

                    Image(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .clickable {
                                onEventDispatcher.invoke(
                                    PlayContract.Intent.UserAction(
                                        CommandEnum.MANAGE
                                    )
                                )
                            },
                        painter = painterResource(
                            id = if (musicIsPlaying.value) R.drawable.pause_button
                            else R.drawable.play_button
                        ),
                        contentDescription = null
                    )

                    Image(
                        modifier = Modifier
                            .rotate(180f)
                            .size(50.dp)
                            .padding(8.dp)
                            .clickable {
                                onEventDispatcher.invoke(PlayContract.Intent.UserAction(CommandEnum.NEXT))
                                seekBarValue = 0
                            },
                        painter = painterResource(id = R.drawable.previous),
                        contentDescription = null
                    )



                    Icon(
                        painter = painterResource(id = if (isSaved) R.drawable.heart2 else R.drawable.heart1),

                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.dp)

                            .padding(8.dp)
                            .clickable {
                                if (isSaved) {

                                    onEventDispatcher(PlayContract.Intent.DeleteMusic(musicData.value!!))
                                } else {

                                    onEventDispatcher(PlayContract.Intent.SaveMusic(musicData.value!!))
                                }
                            },
                        contentDescription = null,
                    )
                }
            }
        }
    }


    @Preview
    @Composable
    fun VinlyPrev() {
        VinylAnimation(isPlaying = true)
    }

    @Composable
    fun VinylAnimation(
        modifier: Modifier = Modifier,
        isPlaying: Boolean = false
    ) {
        // Allow resume on rotation
        var currentRotation by remember { mutableStateOf(0f) }

        val rotation = remember { Animatable(currentRotation) }

        LaunchedEffect(isPlaying) {
            if (isPlaying) {
                // Infinite repeatable rotation when is playing
                rotation.animateTo(
                    targetValue = currentRotation + 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                ) {
                    currentRotation = value
                }
            } else {
                if (currentRotation > 0f) {
                    // Slow down rotation on pause
                    rotation.animateTo(
                        targetValue = currentRotation + 50,
                        animationSpec = tween(
                            durationMillis = 1250,
                            easing = LinearOutSlowInEasing
                        )
                    ) {
                        currentRotation = value
                    }
                }
            }
        }
        Vinyl(modifier = modifier.padding(24.dp), rotationDegrees = rotation.value)
    }


    @Composable
    fun Vinyl(
        modifier: Modifier = Modifier,
        rotationDegrees: Float = 0f
    ) {
        Box(
            modifier = modifier
                .aspectRatio(1.0f)
        ) {

            // Vinyl background
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(rotationDegrees),
                painter = painterResource(id = R.drawable.music_disk),
                contentDescription = ""
            )

            // Vinyl lights effect


            // Vinyl 'album' cover
            // For using with Coil or Glide, wrap into surface with shape

        }
    }

}
