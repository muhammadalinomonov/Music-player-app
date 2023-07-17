package uz.gita.mymusicplayer.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import uz.gita.mymusicplayer.data.model.MusicData

private val projection = arrayOf(
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.DATA,
    MediaStore.Audio.Media.DURATION,
    MediaStore.Audio.Media.ALBUM_ID
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

    var albumArt:Bitmap? = null

    /*try {
        val mmr = MediaMetadataRetriever()

        Log.d("TTT", "1-log")
        Log.d("TTT", this.getString(3))
        mmr.setDataSource(this.getString(3))
        Log.d("TTT", "2 -log")
        albumArt = runBlocking(Dispatchers.IO) {
            getBitmap(mmr.embeddedPicture)
        }
        mmr.release()
        Log.d("TTT", "3 -log")
    }catch (e:Exception){
        albumArt = null
    }*/

    return MusicData(
        id = this.getInt(0),
        artist = this.getString(1),
        title = this.getString(2),
        data = this.getString(3),
        duration = this.getLong(4),
        albumArt = albumArt
    )
}
private suspend fun getBitmap(byteArray: ByteArray?): Bitmap? {
    if (byteArray != null) {
        return withContext(Dispatchers.Default) {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }
    return null
}