package ru.ok.itmo.tamtam.ui.start

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.ok.itmo.tamtam.custom_fragment.CustomFragment
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.server.RequestUiState

class SplashFragment : CustomFragment(R.layout.fragment_splash) {
    private val viewModel: SplashViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    companion object {
        const val ARG_SPLASH_TYPE = "splashType"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        val type =
            arguments?.getSerializable(ARG_SPLASH_TYPE) as? SplashType
                ?: throw IllegalArgumentException("I don't know splash type")

        progressBar = view.findViewById(R.id.progress_bar)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RequestUiState.Wait -> showLoading()
                is RequestUiState.Success -> {
                    hideLoading()
                    findNavController().navigate((state as SplashViewModel.Success).direction)
                }

                is RequestUiState.Error -> {
                    hideLoading()
                    Toast.makeText(requireContext(), state.e.toString(), Toast.LENGTH_LONG).show()
                    findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToLoginNavGraph()
                    )
                }
            }
        }

        if (savedInstanceState == null) {
            viewModel.getDirection(type)
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.INVISIBLE
    }
}