package ru.ok.itmo.tamtam.helper

import android.content.Context
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Button.setColor(context: Context, color: Int) {
    val tintedBackground = DrawableCompat.wrap(background).mutate()
    DrawableCompat.setTint(tintedBackground, ContextCompat.getColor(context, color))
    background = tintedBackground
}