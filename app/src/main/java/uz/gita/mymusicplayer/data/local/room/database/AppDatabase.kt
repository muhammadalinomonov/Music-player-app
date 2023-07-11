package uz.gita.mymusicplayer.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.mymusicplayer.data.local.room.dao.MusicDao
import uz.gita.mymusicplayer.data.local.room.entity.MusicEntity

@Database(entities = [MusicEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMusicDao(): MusicDao
}