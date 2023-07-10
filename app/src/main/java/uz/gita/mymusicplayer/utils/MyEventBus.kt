package uz.gita.mymusicplayer.utils

import android.database.Cursor
import kotlinx.coroutines.flow.MutableStateFlow
import uz.gita.mymusicplayer.data.model.MusicData

object MyEventBus {
    var selectMusicPos: Int = -1
    var cursor: Cursor? = null

    var totalTime: Int = 0
    var currentTime = MutableStateFlow(0)
    val currentTimeFlow = MutableStateFlow(0)

    var isPlaying = MutableStateFlow(false)
    val currentMusicData = MutableStateFlow<MusicData?>(null)
}