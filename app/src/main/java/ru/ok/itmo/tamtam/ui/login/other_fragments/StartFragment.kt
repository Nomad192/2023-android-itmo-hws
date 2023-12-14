package ru.ok.itmo.tamtam.ui.login.other_fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import ru.ok.itmo.tamtam.custom_fragment.CustomFragment
import ru.ok.itmo.tamtam.R

class StartFragment : CustomFragment(R.layout.fragment_start) {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_login_authorization).setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToLoginFragment())
        }

        view.findViewById<Button>(R.id.btn_phone_authorization).setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToLoginPhoneFragment())
        }
    }
}