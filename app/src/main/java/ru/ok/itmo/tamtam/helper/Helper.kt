package ru.ok.itmo.tamtam.helper

import android.content.Context
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

class Helper {
    companion object {
        const val DEBUG_TAG = "ru.ok.itmo.hw_debug"

        fun setButtonColor(button: Button, context: Context, color: Int) {
            val tintedBackground = DrawableCompat.wrap(button.background).mutate()
            DrawableCompat.setTint(tintedBackground, ContextCompat.getColor(context, color))
            button.background = tintedBackground
        }
    }
}