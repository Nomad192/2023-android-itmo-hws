package ru.ok.itmo.tamtam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.ok.itmo.tamtam.token.TokenModel
import ru.ok.itmo.tamtam.token.TokenRepository

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val koinApp = startKoin {
                androidContext(this@MainActivity)
                modules(appModule)
            }.koin

            val tokenRepository: TokenRepository by koinApp.inject()

            tokenRepository.init(this)
        }
    }
}

val appModule = module {
    single { TokenRepository() }
    single<TokenModel> {
        get<TokenRepository>()
    }
}