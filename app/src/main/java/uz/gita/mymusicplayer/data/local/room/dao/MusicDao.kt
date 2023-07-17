package uz.gita.mymusicplayer.data.local.room.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.gita.mymusicplayer.data.local.room.entity.MusicEntity

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMusic(musicEntity: MusicEntity)


    @Query("SELECT exists (SELECT * FROM musics WHERE data = :data)")
    fun checkMusicSaved(data: String): Boolean

    @Delete
    fun deleteMusicSaved(musicEntity: MusicEntity)

    @Query("SELECT * FROM musics")
    fun getAllMusics(): Flow<List<MusicEntity>>

    @Query("SELECT * FROM musics")
    fun getSavedMusics(): Cursor
}
