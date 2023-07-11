package uz.gita.mymusicplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.mymusicplayer.presentation.screen.favourite.FavouriteContact
import uz.gita.mymusicplayer.presentation.screen.favourite.FavouriteDirections
import uz.gita.mymusicplayer.presentation.screen.musiclist.MusicListContract
import uz.gita.mymusicplayer.presentation.screen.musiclist.MusicListDirectionImpl
import uz.gita.mymusicplayer.presentation.screen.play.PlayContract
import uz.gita.mymusicplayer.presentation.screen.play.PlayDirectionImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {
    @Binds
    fun bindMusicListDirection(impl: MusicListDirectionImpl): MusicListContract.Directions

    @Binds
    fun bindPlayDirection(impl: PlayDirectionImpl): PlayContract.Directions

    @Binds
    fun bindFavouriteDirection(impl: FavouriteDirections): FavouriteContact.Directions


}