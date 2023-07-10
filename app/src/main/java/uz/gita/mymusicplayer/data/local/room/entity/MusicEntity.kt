package uz.gita.mymusicplayer.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.mymusicplayer.data.model.MusicData

@Entity(tableName = "musics")
data class MusicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val artist: String?,
    val title: String?,
    val data: String?,
    val duration: Long,
    val storagePosition: Int
) {
    fun toMusicData() = MusicData(id, artist, title, data, duration, storagePosition)
}
