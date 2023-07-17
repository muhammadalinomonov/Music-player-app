package uz.gita.mymusicplayer.domain.repository

import android.database.Cursor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.gita.mymusicplayer.data.local.room.dao.MusicDao
import uz.gita.mymusicplayer.data.local.room.entity.MusicEntity
import uz.gita.mymusicplayer.data.model.MusicData
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(private val dao: MusicDao) : AppRepository {
    override fun addToFavourite(musicEntity: MusicEntity) {
        return dao.addMusic(musicEntity.copy(storagePosition = 1))
    }

    override fun removeFromFavourite(musicEntity: MusicEntity) {
        return dao.deleteMusicSaved(musicEntity)
    }

    override fun getFavouriteMusics(): Cursor = dao.getSavedMusics()

    override fun checkSavedMusic(musicData: MusicData): Boolean =
        dao.checkMusicSaved(musicData.data ?: "")

    override fun getAllMusics(): Flow<List<MusicData>> {
        return dao.getAllMusics().map { it.map { it.toMusicData() } }
    }
}