package ru.ok.itmo.tamtam.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.ok.itmo.tamtam.server.RequestUiState
import ru.ok.itmo.tamtam.server.ServerException
import ru.ok.itmo.tamtam.token.TokenRepository

class SplashViewModel : ViewModel(), KoinComponent {
    private val tokenRepositoryInstance: TokenRepository by inject()
    data class Success(val direction: NavDirections) : RequestUiState.Success()

    private val _state: MutableLiveData<RequestUiState> = MutableLiveData()
    val state: LiveData<RequestUiState> = _state

    private fun sendToLogin() {
        _state.value = Success(
            SplashFragmentDirections.actionSplashFragmentToLoginNavGraph()
        )
    }

    private fun sendToApp(channelsJson: String) {
        _state.value = Success(
            SplashFragmentDirections.actionSplashFragmentToAppNavGraph(channelsJson)
        )
    }

    fun getDirection(type: SplashType) {
        viewModelScope.launch {
            when (type) {
                SplashType.LOGOUT_TYPE -> logout(this)
                SplashType.CHECK_TYPE -> checkAuthAndGetChannels()
            }
        }
    }

    private fun logout(coroutineScope: CoroutineScope) {
        tokenRepositoryInstance.logout()
            .flowOn(Dispatchers.IO)
            .catch {
                when (it) {
                    is ServerException.NoNet -> sendToLogin()
                    else -> _state.value = RequestUiState.Error(it)
                }
            }
            .onStart { sendToLogin() }
            .launchIn(coroutineScope)
    }

    private suspend fun checkAuthAndGetChannels() {
        _state.value = RequestUiState.Wait()
        tokenRepositoryInstance.checkAuthAndGetChannels()
            .flowOn(Dispatchers.IO)
            .catch {
                when (it) {
                    is ServerException.Unauthorized -> sendToLogin()
                    is ServerException.NoNet -> sendToApp("NoNet")
                    else -> _state.value = RequestUiState.Error(it)
                }
            }
            .collect { sendToApp(it.toString()) }
    }
}