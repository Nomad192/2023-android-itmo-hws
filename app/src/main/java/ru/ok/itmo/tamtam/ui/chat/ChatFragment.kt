package ru.ok.itmo.tamtam.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import ru.ok.itmo.tamtam.custom_fragment.CustomFragment
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.helper.closeAll

class ChatFragment : CustomFragment(R.layout.fragment_chat) {
    private lateinit var name: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            name =
                it.getString(ARG_NAME) ?: throw IllegalArgumentException("I don't know chat name")
        }
        view.findViewById<TextView>(R.id.fragment_chat_navbar_title).text = name

        val textView = view.findViewById<TextView>(R.id.my_chat_text)
        textView.text = "$name get data? wait"

        view.findViewById<ImageView>(R.id.arrow_left_back).setOnClickListener {
            closeAll()
            requireActivity().supportFragmentManager
        }

//        lifecycleScope.launch {
//            modelInstance.getMessage(object : OnDataReadyCallbackString {
//                override fun onDataReady(jsonString: String) {
//                    val gson = Gson()
//                    val jsonArray = gson.fromJson(jsonString, Array<JsonObject>::class.java)
//                    var result = ""
//                    for (jsonObject in jsonArray) {
//                        val from = jsonObject.getAsJsonPrimitive("from").asString
//                        val data = jsonObject.getAsJsonObject("data")
//
//                        result += "From: $from, Data: ${data.toString()}\n"
//                    }
//
//                    textView.text = result
//                }
//            })
//        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closeAll()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    companion object {
        private const val ARG_NAME = "name"

        @JvmStatic
        fun newInstance(name: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                }
            }
    }
}