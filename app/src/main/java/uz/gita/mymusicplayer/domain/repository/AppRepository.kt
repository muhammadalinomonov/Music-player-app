package uz.gita.mymusicplayer.domain.repository

import android.database.Cursor
import kotlinx.coroutines.flow.Flow
import uz.gita.mymusicplayer.data.local.room.entity.MusicEntity
import uz.gita.mymusicplayer.data.model.MusicData

interface AppRepository {
    fun addToFavourite(musicEntity: MusicEntity)
    fun removeFromFavourite(musicEntity: MusicEntity)

    fun getFavouriteMusics(): Cursor

    fun checkSavedMusic(musicData: MusicData):Boolean

    fun getAllMusics(): Flow<List<MusicData>>
}