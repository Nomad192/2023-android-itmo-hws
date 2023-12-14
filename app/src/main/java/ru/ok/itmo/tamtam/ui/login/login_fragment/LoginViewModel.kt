package ru.ok.itmo.tamtam.ui.login.login_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.ok.itmo.tamtam.server.RequestUiState
import ru.ok.itmo.tamtam.server.ServerWorker
import ru.ok.itmo.tamtam.token.TokenRepository

class LoginViewModel : ViewModel(), KoinComponent {
    var readyForAuthorization: MutableLiveData<Boolean> = MutableLiveData(false)
    private val tokenRepositoryInstance: TokenRepository by inject()
    private val serverWorker: ServerWorker by lazy { ServerWorker.getInstance() }

    private var isLoginReady: Boolean = false
    private var isPasswordReady: Boolean = false
    private var login: String = ""
    private var password: String = ""

    var state: MutableLiveData<RequestUiState> = MutableLiveData()

    fun authorization() {
        state.value = RequestUiState.Wait()

        viewModelScope.launch {
            serverWorker.login(login, password)
                .flowOn(Dispatchers.IO)
                .catch {
                    state.value = RequestUiState.Error(it)
                }
                .collect { token ->
                    tokenRepositoryInstance.saveToken(token)
                    state.value = RequestUiState.Success()
                }
        }
    }

    fun changeLogin(newLogin: String) {
        login = newLogin
        isLoginReady = newLogin.isNotEmpty()
        updateReadyForAuthorization()
    }

    fun changePassword(newPassword: String) {
        password = newPassword
        isPasswordReady = newPassword.isNotEmpty()
        updateReadyForAuthorization()
    }

    private fun updateReadyForAuthorization() {
        readyForAuthorization.value = isLoginReady && isPasswordReady
    }
}