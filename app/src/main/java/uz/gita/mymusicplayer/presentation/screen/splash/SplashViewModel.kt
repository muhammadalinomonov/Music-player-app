package uz.gita.mymusicplayer.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.mymusicplayer.navigation.AppNavigator
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val navigator: AppNavigator) :ViewModel(){
    init {
        viewModelScope.launch {
            delay(1500)
//            todo navigator.replace(MusicListScreen())
        }
    }
}