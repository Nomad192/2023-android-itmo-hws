package ru.ok.itmo.tamtam.custom_fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class CustomFragment : Fragment {

    constructor() : super()

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setColorIconsOnStatusBar(view)

        super.onViewCreated(view, savedInstanceState)

        setPaddingForStatusVar(view)
    }

    private fun setColorIconsOnStatusBar(view: View) {
        val decorView = requireActivity().window.decorView
        val backgroundColor = (view.background as? ColorDrawable)?.color
            ?: throw IllegalArgumentException("Set background color")

        @Suppress("DEPRECATION")
        val systemUiVisibilityFlags =
            if (backgroundColor == Color.WHITE) {
                decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {
                decorView.systemUiVisibility.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
            }
        @Suppress("DEPRECATION")
        decorView.systemUiVisibility = systemUiVisibilityFlags
    }

    private fun setPaddingForStatusVar(view: View) {
        view.setOnApplyWindowInsetsListener { currentView, insets ->
            @Suppress("DEPRECATION")
            currentView.setPadding(
                0,
                insets.systemWindowInsetTop,
                0,
                insets.systemWindowInsetBottom
            )

            return@setOnApplyWindowInsetsListener insets
        }
    }
}
