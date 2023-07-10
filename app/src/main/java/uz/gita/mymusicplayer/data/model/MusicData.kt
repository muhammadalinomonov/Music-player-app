package uz.gita.mymusicplayer.data.model

import uz.gita.mymusicplayer.data.local.room.entity.MusicEntity

data class MusicData(
    val id: Int,
    val artist: String?,
    val title: String?,
    val data: String?,
    val duration: Long,
    val storagePosition: Int = 0
) {
    fun toEntity() = MusicEntity(id, artist, title, data, duration, storagePosition)
}