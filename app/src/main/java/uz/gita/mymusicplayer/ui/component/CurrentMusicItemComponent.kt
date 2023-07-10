package uz.gita.mymusicplayer.ui.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import uz.gita.mymusicplayer.R
import uz.gita.mymusicplayer.data.model.CommandEnum
import uz.gita.mymusicplayer.presentation.screen.musiclist.MusicListContract
import uz.gita.mymusicplayer.utils.MyEventBus
import uz.gita.mymusicplayer.utils.getMusicDataByPosition

@OptIn(ExperimentalUnitApi::class)
@Composable
fun CurrentMusicItemComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onEventDispatcher: (MusicListContract.Intent) -> Unit,

    ) {

    var musicData by remember {
        mutableStateOf(MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.selectMusicPos))
    }

    LaunchedEffect(key1 = MyEventBus.selectMusicPos){
        musicData = MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.selectMusicPos)
    }

    val musicIsPlaying = MyEventBus.isPlaying.collectAsState()

    val scrollState = rememberScrollState()
    var shouldAnimated by remember { mutableStateOf(true) }

    // Marque effect
    LaunchedEffect(key1 = shouldAnimated) {
        scrollState.animateScrollTo(
            scrollState.maxValue,
            animationSpec = tween(10000, 200, easing = CubicBezierEasing(0f, 0f, 0f, 0f))
        )
        scrollState.scrollTo(0)
        shouldAnimated = !shouldAnimated
    }

    Surface(
        color = Color(0xFF69AADF),
        modifier = modifier
            .padding(8.dp)
            .wrapContentHeight()
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .clickable { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_music),
                contentDescription = "MusicDisk",
                modifier = Modifier
                    .padding(8.dp)
                    .width(45.dp)
                    .height(35.dp)
                    .align(Alignment.CenterVertically)
                //.background(Color(0XFF988E8E), RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = musicData.title ?: "-- -- --",
                    color = Color.Black,
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.horizontalScroll(scrollState, false)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = musicData.artist ?: "Unknown artist",
                    color = Color.Gray,
                    fontSize = TextUnit(14f, TextUnitType.Sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            Image(
                //colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .size(35.dp)
                    .clip(CircleShape)
                    .clickable {
                        musicData =
                            MyEventBus.cursor!!.getMusicDataByPosition(if (MyEventBus.selectMusicPos > 0) MyEventBus.selectMusicPos - 1 else MyEventBus.cursor!!.count - 1)
                        onEventDispatcher(MusicListContract.Intent.UserCommand(CommandEnum.PREV))
                    },
                painter = painterResource(
                    id = R.drawable.previous

                ),
                contentDescription = null
            )

            Image(
                //colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .size(35.dp)
                    .clip(CircleShape)
                    .clickable { onEventDispatcher(MusicListContract.Intent.UserCommand(CommandEnum.MANAGE)) },
                painter = painterResource(
                    id = if (musicIsPlaying.value) R.drawable.pause_button
                    else R.drawable.play_button
                ),
                contentDescription = null
            )
            Image(
                //colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .size(35.dp)
                    .clip(CircleShape)
                    .clickable {
                        musicData =
                            MyEventBus.cursor!!.getMusicDataByPosition(if (MyEventBus.selectMusicPos < MyEventBus.cursor!!.count- 1) MyEventBus.selectMusicPos + 1 else 0)
                        onEventDispatcher(MusicListContract.Intent.UserCommand(CommandEnum.NEXT))
                    }
                    .rotate(180f),
                painter = painterResource(id = R.drawable.previous),

                contentDescription = null
            )

        }
    }
}