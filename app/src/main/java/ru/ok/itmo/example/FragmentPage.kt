package ru.ok.itmo.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import ru.ok.itmo.example.fragment_with_navigation.FragmentWithNavigation
import ru.ok.itmo.example.fragment_with_navigation.MenuData
import java.text.SimpleDateFormat


class FragmentPage : Fragment(R.layout.fragment_page), ViewModelStoreOwner {
    companion object {

        object TAGS {
            const val SECTION_TITLE = "sectionTitle"
            const val PAGE_NUMBER = "pageNumber"
            const val RANDOM_NUMBER = "randomNumber"
            const val RANDOM_NUMBER_BUNDLE = "randomNumberBundle"
            const val RANDOM_NUMBER_ARG = "randomNumberArg"
            const val CREATE_TIME = "createTime"
        }

        private const val UPDATE_INTERVAL = 100
        private const val DATE_FORMAT_PATTERN = "HH:mm:ss.S"

        fun newInstance(sectionTitle: CharSequence) = FragmentPage().apply {
            arguments = bundleOf(
                TAGS.SECTION_TITLE to sectionTitle, TAGS.CREATE_TIME to System.currentTimeMillis()
            )
        }

        class FragmentPageSharedViewModel(private val savedStateHandle: SavedStateHandle) :
            ViewModel() {
            var savedRandomNumber: Int
                get() = savedStateHandle[TAGS.RANDOM_NUMBER] ?: -1
                set(value) {
                    savedStateHandle[TAGS.RANDOM_NUMBER] = value
                }

            fun isStoreRandomNumber(): Boolean {
                return savedStateHandle.contains(TAGS.RANDOM_NUMBER)
            }

            fun restoreStateFromBundle(savedInstanceState: Bundle?, title: String) {
                Log.d(
                    "random_n",
                    "restore: $title, rn ${savedRandomNumber}, saved ${
                        savedStateHandle.get<Int>(TAGS.RANDOM_NUMBER)
                    }, Bundle ${
                        savedInstanceState?.getInt(TAGS.RANDOM_NUMBER_BUNDLE, -2).toString()
                    }"
                )

                if (savedStateHandle.get<Int>(TAGS.RANDOM_NUMBER) == null && savedInstanceState != null) {
                    savedRandomNumber = savedInstanceState.getInt(TAGS.RANDOM_NUMBER_BUNDLE)
                }
            }

            fun saveStateInBundle(outState: Bundle, localRandomNumber: Int, title: String) {
                if (savedRandomNumber == -1 && localRandomNumber == -1)
                    Log.d(
                        "random_n",
                        "NOT save: $title, Bundle ${
                            outState.getInt(
                                TAGS.RANDOM_NUMBER_BUNDLE,
                                -2
                            ).toString()
                        }"
                    )
                else {

                    Log.d("random_n", "save: $title, saved $savedRandomNumber, local $localRandomNumber")

                    if (savedRandomNumber != -1)
                        outState.putInt(TAGS.RANDOM_NUMBER_BUNDLE, savedRandomNumber)
                    else
                        outState.putInt(TAGS.RANDOM_NUMBER_BUNDLE, localRandomNumber)
                }
            }
        }
    }

    private val viewModel: FragmentPageSharedViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
//    private lateinit var viewModelStoreOwner: FragmentPageSharedViewModel

    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
    private lateinit var currentTimeTextView: TextView
    private var randomNumber: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.bundle_value).text =
            (savedInstanceState != null).toString()
        view.findViewById<TextView>(R.id.saved_state_handle_value).text =
            (viewModel.isStoreRandomNumber()).toString()

        view.findViewById<TextView>(R.id.arg_random_number_value).text =
            (arguments?.getInt(TAGS.RANDOM_NUMBER_ARG, -2)).toString()

//        viewModelStoreOwner = ViewModelProvider(this)[FragmentPageSharedViewModel::class.java]

        val sectionTitle = arguments?.getCharSequence(TAGS.SECTION_TITLE)
            ?: throw IllegalArgumentException("I don't know section title")
        val createTime = arguments?.getLong(TAGS.CREATE_TIME)
            ?: throw IllegalArgumentException("I don't know create time")
        viewModel.restoreStateFromBundle(savedInstanceState, sectionTitle.toString())

        if (viewModel.savedRandomNumber == -1) viewModel.savedRandomNumber = (1..50).random()
        randomNumber = viewModel.savedRandomNumber

        if (arguments?.getInt(TAGS.RANDOM_NUMBER_ARG, -2) == -2)
        {
            arguments?.putInt(TAGS.RANDOM_NUMBER_ARG, randomNumber)
        }

//        view.findViewById<TextView>(R.id.store_owner_handle_value).text =
//            (viewModelStoreOwner.savedRandomNumber != null).toString()
//
//        randomNumber = getRandomValue(savedInstanceState)

        view.findViewById<TextView>(R.id.section_title_value).text = sectionTitle.toString()
        view.findViewById<TextView>(R.id.page_value).text = "???"
        view.findViewById<TextView>(R.id.random_number_value).text =
            randomNumber.toString()
        view.findViewById<TextView>(R.id.creation_time_value).text = timeFormat.format(createTime)

        currentTimeTextView = view.findViewById(R.id.current_time_value)
//        retainInstance = true
//        setRetainInstance(true)
    }

//    private fun getRandomValue(savedInstanceState: Bundle?): Int =
//        viewModel.savedRandomNumber
//            ?: savedInstanceState?.getInt(TAGS.RANDOM_NUMBER_BUNDLE)
//                .also { viewModel.savedRandomNumber = it }
//            ?: (0..50).random()
//                .also { viewModel.savedRandomNumber = it }

//    private fun getRandomValue(savedInstanceState: Bundle?): Int =
//        viewModel.savedRandomNumber
//            ?: (0..50).random()
//                .also { viewModel.savedRandomNumber = it }

    override fun onResume() {
        super.onResume()
        updateCurrentTime()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    private fun updateCurrentTime() {
        val currentTime = System.currentTimeMillis()
        val formattedTime = timeFormat.format(currentTime)
        currentTimeTextView.text = formattedTime

        handler.postDelayed({ updateCurrentTime() }, UPDATE_INTERVAL.toLong())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        parentFragmentManager.putFragment(outState, "myFragmentName", mMyFragment!!)
//        outState.putInt(TAGS.RANDOM_NUMBER_BUNDLE, randomNumber)
        viewModel.saveStateInBundle(
            outState,
            randomNumber,
            arguments?.getCharSequence(TAGS.SECTION_TITLE).toString()
        )
    }

    override fun onStart() {
        super.onStart()
        Log.d(
            "random_n",
            "onStart: FragmentPage ${arguments?.getCharSequence(TAGS.SECTION_TITLE)!!}, rand $randomNumber"
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(
            "random_n",
            "onViewStateRestored: FragmentPage ${arguments?.getCharSequence(TAGS.SECTION_TITLE)!!}, rand $randomNumber"
        )
    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        viewModel.restoreStateFromBundle(savedInstanceState, arguments?.getCharSequence(TAGS.SECTION_TITLE).toString())
//
////        parentFragmentManager.putFragment(outState, "myFragmentName", mMyFragment!!)
////        outState.putInt(TAGS.RANDOM_NUMBER_BUNDLE, randomNumber)
//    }

//
//    override fun onDestroyView() {
//        Log.d(
//            "onDestroyView",
//            "FragmentPage ${arguments?.getCharSequence(TAGS.SECTION_TITLE)!!}, rand $randomNumber"
//        )
//        super.onDestroyView()
//    }
}