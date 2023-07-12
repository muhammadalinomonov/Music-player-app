package uz.gita.mymusicplayer.presentation.screen.tabs


import uz.gita.mymusicplayer.navigation.AppScreen
import uz.gita.mymusicplayer.presentation.screen.favourite.FavouriteScreen
import uz.gita.mymusicplayer.presentation.screen.musiclist.MusicListScreen

val tabs = listOf(
    TabItem(
        title = "All tracks",
        screen = MusicListScreen()
    ),
    TabItem(
        title = "Favorite",
        screen = FavouriteScreen()
    )
)

data class TabItem(
    val title: String,
    val screen: AppScreen
)


