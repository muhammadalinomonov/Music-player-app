package uz.gita.mymusicplayer.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
        .padding(vertical = 0.dp, horizontal = 4.dp)
        .fillMaxWidth()

        .background(Color.White)
        .height(64.dp)

        .combinedClickable(
            onClick = { onClick.invoke() },
            onLongClick = { /*onLongClick.invoke()*/ }
        )
    ) {

        Row(modifier = Modifier.fillMaxHeight()) {


            if (musicData.albumArt != null)
                Image(
                    bitmap = musicData.albumArt.asImageBitmap(),
                    contentDescription = "MusicDisk",
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .width(50.dp)
                        .height(50.dp)
                        .align(Alignment.CenterVertically)
                    //.background(Color(0XFF94E4E), RoundedCornerShape(4.dp))
                )
            else {
                Image(
                    painterResource(id = R.drawable.ic_music2),

                    contentDescription = "MusicDisk",
                    modifier = Modifier
                        .padding(4.dp)
                        .width(50.dp)
                        .height(50.dp)
                        .align(Alignment.CenterVertically)
                    //.background(Color(0XFF988E8E), RoundedCornerShape(8.dp))
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = musicData.title ?: "-- -- --",
                    color = Color.Black,
                    fontSize = TextUnit(16f, TextUnitType.Sp),
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
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

                Spacer(modifier = Modifier
                    .height(0.dp)
                    .weight(1f))
                Row(modifier = Modifier
                    .padding(top = 1.dp, bottom = 2.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)) {}
            }
        }
    }
}