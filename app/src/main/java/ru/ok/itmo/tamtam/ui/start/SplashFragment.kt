package ru.ok.itmo.tamtam.ui.start

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.ok.itmo.tamtam.custom_fragment.CustomFragment
import ru.ok.itmo.tamtam.R

class SplashFragment : CustomFragment(R.layout.fragment_splash) {
    private val viewModel: SplashViewModel by viewModels()
    private lateinit var type: SplashType

    companion object {
        const val ARG_SPLASH_TYPE = "splashType"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        type =
            arguments?.getSerializable(ARG_SPLASH_TYPE) as? SplashType
                ?: throw IllegalArgumentException("I don't know splash type")

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SplashViewModel.State.Success -> findNavController().navigate(state.direction)
                is SplashViewModel.State.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), state.e.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        if (savedInstanceState == null) {
            viewModel.getDirection(type)
        }
    }
}