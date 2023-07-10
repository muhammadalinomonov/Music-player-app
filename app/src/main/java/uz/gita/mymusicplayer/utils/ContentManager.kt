package uz.gita.mymusicplayer.utils

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.mymusicplayer.data.model.MusicData

private val projection = arrayOf(
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.DATA,
    MediaStore.Audio.Media.DURATION
)

fun Context.getMusicCursor(): Flow<Cursor> = flow {
    val cursor: Cursor = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        MediaStore.Audio.Media.IS_MUSIC + "!=0",
        null,
        null
    ) ?: return@flow
    emit(cursor)
}

fun Cursor.getMusicDataByPosition(pos: Int): MusicData {
    this.moveToPosition(pos)
    return MusicData(
        id = this.getInt(0),
        artist = this.getString(1),
        title = this.getString(2),
        data = this.getString(3),
        duration = this.getLong(4)
    )
}