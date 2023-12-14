package ru.ok.itmo.tamtam.token

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import org.koin.core.component.KoinComponent
import ru.ok.itmo.tamtam.server.ServerException
import ru.ok.itmo.tamtam.server.ServerWorker

class TokenRepository : TokenModel(), KoinComponent {
    companion object {
        private const val TOKEN_KEY = "token"
        private const val DEFAULT_STRING = ""
    }

    private val serverWorker: ServerWorker by lazy { ServerWorker.getInstance() }
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        this.context = context

        @Suppress("DEPRECATION")
        sharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
        this.token = token
    }

    private fun clearToken() {
        saveToken(DEFAULT_STRING)
    }

    fun logout(): Flow<Unit> = serverWorker.logout()
        .onStart {
        clearToken()
    }

    fun checkAuthAndGetChannels(): Flow<List<String>> = serverWorker.getChannels()
        .onStart {
            val token = sharedPreferences.getString(TOKEN_KEY, DEFAULT_STRING)
                ?: throw IllegalArgumentException("sharedPreferences is not init")

            if (token == DEFAULT_TOKEN || token == DEFAULT_STRING)
                throw ServerException.Unauthorized

            this@TokenRepository.token = token
        }
}