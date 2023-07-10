package uz.gita.mymusicplayer.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import uz.gita.mymusicplayer.data.local.room.entity.MusicEntity

@Dao
interface MusicDao {
    @Insert
    fun addMusic(musicEntity: MusicEntity)


}