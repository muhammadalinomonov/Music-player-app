package uz.gita.mymusicplayer.ui.component

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import uz.gita.mymusicplayer.R
import uz.gita.mymusicplayer.data.model.CommandEnum
import uz.gita.mymusicplayer.data.model.CursorEnum
import uz.gita.mymusicplayer.presentation.screen.musiclist.MusicListContract
import uz.gita.mymusicplayer.utils.MyEventBus
import uz.gita.mymusicplayer.utils.getMusicDataByPosition

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CurrentMusicItemComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onEventDispatcher: (MusicListContract.Intent) -> Unit,

    ) {


    var musicData = MyEventBus.currentMusicData.collectAsState().value!!




    LaunchedEffect(key1 = MyEventBus.storagePos) {
        musicData = MyEventBus.storageCursor!!.getMusicDataByPosition(MyEventBus.storagePos)
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
            if (musicData.albumArt != null)
                Image(
                    bitmap = musicData.albumArt!!.asImageBitmap(),
                    /* painter = if (!albumArtUri.isAbsolute || !albumArtUri.isHierarchical  || !albumArtUri.isOpaque || (musicData.albumId == 9089203031363493168 || musicData.albumId == 6539316500227728566)) {
                         painterResource(id = R.drawable.ic_music)
                     } else {
                         rememberAsyncImagePainter(albumArtUri)
                     },*/
                    contentDescription = "MusicDisk",
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .width(45.dp)
                        .height(45.dp)
                        .align(Alignment.CenterVertically)
                    //.background(Color(0XFF988E8E), RoundedCornerShape(8.dp))
                )
            else {
                Image(
                    painterResource(id = R.drawable.music_disk),
                    /* painter = if (!albumArtUri.isAbsolute || !albumArtUri.isHierarchical  || !albumArtUri.isOpaque || (musicData.albumId == 9089203031363493168 || musicData.albumId == 6539316500227728566)) {
                         painterResource(id = R.drawable.ic_music)
                     } else {
                         rememberAsyncImagePainter(albumArtUri)
                     },*/
                    contentDescription = "MusicDisk",
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))

                        .width(45.dp)
                        .height(45.dp)
                        .align(Alignment.CenterVertically)
                    //.background(Color(0XFF988E8E), RoundedCornerShape(8.dp))
                )
            }

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
                    .size(30.dp)
                    .padding(4.dp)
                    .clickable {
                        musicData =
                            MyEventBus.storageCursor!!.getMusicDataByPosition(if (MyEventBus.storagePos > 0) MyEventBus.storagePos - 1 else MyEventBus.storageCursor!!.count - 1)
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
                    .size(30.dp)
                    .padding(0.dp)
                    .clip(CircleShape)
                    .clickable {
                        MyEventBus.currentCursorEnum = CursorEnum.STORAGE

                        Log.d("YYY", musicData.artist!!)
                        onEventDispatcher(MusicListContract.Intent.UserCommand(CommandEnum.MANAGE))
                    },
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
                    .size(30.dp)
                    .padding(4.dp)
                    .clickable {
                        musicData =
                            MyEventBus.storageCursor!!.getMusicDataByPosition(if (MyEventBus.storagePos < MyEventBus.storageCursor!!.count - 1) MyEventBus.storagePos + 1 else 0)
                        onEventDispatcher(MusicListContract.Intent.UserCommand(CommandEnum.NEXT))
                    }
                    .rotate(180f),
                painter = painterResource(id = R.drawable.previous),

                contentDescription = null
            )

        }
    }
}

/*
@Composable
fun CurrentMusicItemComponentForFavourite(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onEventDispatcher: (FavouriteContact.Intent) -> Unit,

    ) {

    var musicData = MyEventBus.currentMusicData.collectAsState().value!!

    */
/*var musicData by remember {
        mutableStateOf(MyEventBus.currentMusicData.collectAsState().value!!)
    }*//*



    LaunchedEffect(key1 = MyEventBus.storagePos) {
        musicData = MyEventBus.storageCursor!!.getMusicDataByPosition(MyEventBus.storagePos)
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
            if (musicData.albumArt != null)
                Image(
                    bitmap = musicData.albumArt!!.asImageBitmap(),
                    */
/* painter = if (!albumArtUri.isAbsolute || !albumArtUri.isHierarchical  || !albumArtUri.isOpaque || (musicData.albumId == 9089203031363493168 || musicData.albumId == 6539316500227728566)) {
                         painterResource(id = R.drawable.ic_music)
                     } else {
                         rememberAsyncImagePainter(albumArtUri)
                     },*//*

                    contentDescription = "MusicDisk",
                    modifier = Modifier
                        .padding(8.dp)
                        .width(35.dp)
                        .height(45.dp)
                        .align(Alignment.CenterVertically)
                    //.background(Color(0XFF988E8E), RoundedCornerShape(8.dp))
                )
            else {
                Image(
                    painterResource(id = R.drawable.ic_music),
                    */
/* painter = if (!albumArtUri.isAbsolute || !albumArtUri.isHierarchical  || !albumArtUri.isOpaque || (musicData.albumId == 9089203031363493168 || musicData.albumId == 6539316500227728566)) {
                         painterResource(id = R.drawable.ic_music)
                     } else {
                         rememberAsyncImagePainter(albumArtUri)
                     },*//*

                    contentDescription = "MusicDisk",
                    modifier = Modifier
                        .padding(8.dp)
                        .width(35.dp)
                        .height(45.dp)
                        .align(Alignment.CenterVertically)
                    //.background(Color(0XFF988E8E), RoundedCornerShape(8.dp))
                )
            }

            Column(
                modifier = Modifier
                    .width(0.dp)
                    .weight(1f)
                    .padding(horizontal = 4.dp)
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
                    .clickable {
                        musicData =
                            MyEventBus.storageCursor!!.getMusicDataByPosition(if (MyEventBus.storagePos > 0) MyEventBus.storagePos - 1 else MyEventBus.storageCursor!!.count - 1)
                        onEventDispatcher(FavouriteContact.Intent.UserCommand(CommandEnum.PREV))
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
                    .clickable { onEventDispatcher(FavouriteContact.Intent.UserCommand(CommandEnum.MANAGE)) },
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
                    .clickable {
                        musicData =
                            MyEventBus.storageCursor!!.getMusicDataByPosition(if (MyEventBus.storagePos < MyEventBus.storageCursor!!.count - 1) MyEventBus.storagePos + 1 else 0)
                        onEventDispatcher(FavouriteContact.Intent.UserCommand(CommandEnum.NEXT))
                    }
                    .rotate(180f),
                painter = painterResource(id = R.drawable.previous),

                contentDescription = null
            )

        }
    }
}
*/

