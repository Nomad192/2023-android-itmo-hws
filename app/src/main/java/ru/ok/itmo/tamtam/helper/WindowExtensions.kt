package ru.ok.itmo.tamtam.helper

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.ui.start.SplashFragment
import ru.ok.itmo.tamtam.ui.start.SplashType

fun Button.setColor(context: Context, color: Int) {
    val tintedBackground = DrawableCompat.wrap(background).mutate()
    DrawableCompat.setTint(tintedBackground, ContextCompat.getColor(context, color))
    background = tintedBackground
}

fun Fragment.closeAll() {
    val fragmentManager = requireActivity().supportFragmentManager
    val navController =
        (fragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    navController.popBackStack(R.id.start_nav_graph, true)
    val bundle = Bundle().apply {
        putSerializable(SplashFragment.ARG_SPLASH_TYPE, SplashType.LOGOUT_TYPE)
    }
    navController.navigate(R.id.start_nav_graph, bundle)
}