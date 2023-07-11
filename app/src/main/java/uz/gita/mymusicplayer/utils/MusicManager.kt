package uz.gita.mymusicplayer.utils

import uz.gita.mymusicplayer.presentation.screen.favourite.FavouriteContact

fun checkMusics(onEventDispatcher: (FavouriteContact.Intent) -> Unit) {
    for (i in 0 until MyEventBus.roomCursor!!.count) {
        val roomData = MyEventBus.roomCursor!!.getMusicDataByPosition(i)
        var bool = true
        for (j in 0 until MyEventBus.storageCursor!!.count) {
            val storageData = MyEventBus.storageCursor!!.getMusicDataByPosition(j)
            bool = bool && roomData != storageData
        }
        if (bool) {
            onEventDispatcher.invoke(FavouriteContact.Intent.DeleteMusic(roomData))
            logger("Music deleted = ${roomData.title}")
        }
    }
}