package uz.gita.mymusicplayer.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import uz.gita.mymusicplayer.R
import uz.gita.mymusicplayer.data.model.MusicData

@OptIn(ExperimentalFoundationApi::class, ExperimentalUnitApi::class)
@Composable
fun MusicItem(
    musicData: MusicData,
    onClick: () -> Unit,
) {

    Surface(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 4.dp)
        .combinedClickable(
            onClick = { onClick.invoke() },
            onLongClick = { /*onLongClick.invoke()*/ }
        )
    ) {
        Row(modifier = Modifier.wrapContentHeight()) {


            if (musicData.albumArt != null)
                Image(
                    bitmap = musicData.albumArt.asImageBitmap(),
                    contentDescription = "MusicDisk",
                    modifier = Modifier
                        .padding(8.dp)
                        .width(45.dp)
                        .height(35.dp)
                        .align(Alignment.CenterVertically)
                    //.background(Color(0XFF988E8E), RoundedCornerShape(8.dp))
                )
            else {
                Image(
                    painterResource(id = R.drawable.ic_music),
                    /* painter = if (!albumArtUri.isAbsolute || !albumArtUri.isHierarchical  || !albumArtUri.isOpaque || (musicData.albumId == 9089203031363493168 || musicData.albumId == 6539316500227728566)) {
                         painterResource(id = R.drawable.ic_music)
                     } else {
                         rememberAsyncImagePainter(albumArtUri)
                     },*/
                    contentDescription = "MusicDisk",
                    modifier = Modifier
                        .padding(8.dp)
                        .width(45.dp)
                        .height(35.dp)
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
                    fontSize = TextUnit(16f, TextUnitType.Sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = musicData.artist ?: "-- -- --",
                    color = Color(0XFF988E8E),
                    fontSize = TextUnit(14f, TextUnitType.Sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}