package ru.ok.itmo.tamtam.ui.chat_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.ok.itmo.tamtam.custom_fragment.CustomFragment
import ru.ok.itmo.tamtam.helper.Helper
import ru.ok.itmo.tamtam.R

class ChatListFragment : CustomFragment(R.layout.fragment_chat_list) {
    private lateinit var channels: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments
        val channelsFromArgs = arguments?.getString(ARG_CHANNELS)
        if (channelsFromArgs != null) {
            channels = channelsFromArgs
            Log.d(Helper.DEBUG_TAG, "channels: $channels")
        } else {
            Log.d(Helper.DEBUG_TAG, "TODO: not implement yet")
        }

        if (savedInstanceState == null) {
            findNavController().navigate(
                ChatListFragmentDirections.actionChatListFragmentToChatFragment(
                    "Default chat"
                )
            )
        }
    }

    companion object {
        const val ARG_CHANNELS = "channels"

        @Suppress("UNUSED")
        fun newInstance(channels: String) =
            ChatListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CHANNELS, channels)
                }
            }
    }
}