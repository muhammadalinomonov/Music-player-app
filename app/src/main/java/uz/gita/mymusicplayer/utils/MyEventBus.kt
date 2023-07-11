package uz.gita.mymusicplayer.utils

import android.database.Cursor
import kotlinx.coroutines.flow.MutableStateFlow
import uz.gita.mymusicplayer.data.model.CursorEnum
import uz.gita.mymusicplayer.data.model.MusicData

object MyEventBus {
    var storagePos: Int = -1
    var roomPos: Int = -1
    var storageCursor: Cursor? = null
    var roomCursor: Cursor? = null


    var currentCursorEnum: CursorEnum? = null

    var totalTime: Int = 0
    var currentTime = MutableStateFlow(0)
    val currentTimeFlow = MutableStateFlow(0)

    var isPlaying = MutableStateFlow(false)
    val currentMusicData = MutableStateFlow<MusicData?>(null)
    var isRepeated = false
    var isRepeatedFlow = MutableStateFlow(false)
}