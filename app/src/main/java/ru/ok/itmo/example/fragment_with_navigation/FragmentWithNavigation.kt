package ru.ok.itmo.example.fragment_with_navigation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.ok.itmo.example.FragmentPage
import ru.ok.itmo.example.R

class FragmentWithNavigation : Fragment(R.layout.fragment_with_navigation) {
    private val viewModel: FragmentPage.Companion.FragmentPageSharedViewModel by viewModels()

    companion object {
        private const val NUMBER_OF_SECTIONS = "numberOfSections"
        private const val SELECTED_ITEM_ID = "selectedItemId"
        private const val MENU_DATA = "menuData"

        fun newInstance(numberOfSections: Int) = FragmentWithNavigation().apply {
            arguments = bundleOf(
                NUMBER_OF_SECTIONS to numberOfSections
            )
        }

        class FragmentViewModel : ViewModel() {
            private val fragmentMap = mutableMapOf<String, Fragment>()

            fun addFragment(key: String, fragment: Fragment) {
                fragmentMap[key] = fragment
            }

            fun getFragment(key: String): Fragment? {
                return fragmentMap[key]
            }
        }

        class FragmentWithNavigationSharedViewModel(private val savedStateHandle: SavedStateHandle) :
            ViewModel() {
            val fragmentMap = mutableMapOf<String, Fragment>()

            fun isStoresMasterData(): Boolean {
                Log.d("tag", savedStateHandle.keys().toString())
                return savedStateHandle.contains(MENU_DATA)
            }

            var selectedItemId: Int
                get() = savedStateHandle[SELECTED_ITEM_ID]
                    ?: throw IllegalArgumentException("$SELECTED_ITEM_ID is missing from savedStateHandle")
                set(value) {
                    savedStateHandle[SELECTED_ITEM_ID] = value
                }

            var menuData: MenuData
                get() = savedStateHandle[MENU_DATA]
                    ?: throw IllegalArgumentException("$MENU_DATA is missing from savedStateHandle")
                set(value) {
                    savedStateHandle[MENU_DATA] = value
                }
        }
    }

    //    private val fragmentViewModel: FragmentViewModel by viewModels()
    private val sharedViewModel: FragmentWithNavigationSharedViewModel by viewModels()
    private lateinit var navigationView: NavigationViewInterface

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationView = processNavigationView(view.findViewById(R.id.nav_view))

        val numberOfSections = arguments?.getInt(NUMBER_OF_SECTIONS)
            ?: throw IllegalArgumentException("I don't know how many sections are needed")

        if (sharedViewModel.isStoresMasterData()) {
            drawMenu()
            navigationView.selectedItemId = sharedViewModel.selectedItemId
        } else {
            sharedViewModel.menuData = MenuData(numberOfSections)
            drawMenu()
            val firstItem = navigationView.menu.getItem(0)
            firstItem.isChecked = true
            addNewSection(firstItem.title.toString(), firstItem.itemId.toString())
        }

        navigationView.setOnItemSelectedListener { item ->
            replaceSection(item.title.toString(), item.itemId.toString())
            true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

//                if (parentFragmentManager.backStackEntryCount >= 2) {
                    val lastFragmentTag =
                        parentFragmentManager.getBackStackEntryAt(parentFragmentManager.backStackEntryCount - 2).name

                    if (lastFragmentTag == null) {
                        parentFragmentManager.popBackStack()
                    } else {
                        navigationView.menu.findItem(lastFragmentTag.toInt()).isChecked = true
                    }
//                }

                parentFragmentManager.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun replaceSection(title: String, tag: String) {
        val existingFragment = parentFragmentManager.findFragmentByTag(tag) as? FragmentPage

        if (existingFragment != null) {
            parentFragmentManager.popBackStack(tag, 0)
        } else {
            addNewSection(title, tag)
        }
    }

    private fun addNewSection(title: String, tag: String) {
        var fragment = sharedViewModel.fragmentMap[tag]

        if (fragment == null) {
            fragment = FragmentPage.newInstance(title)
            sharedViewModel.fragmentMap[tag] = fragment
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_with_navigation_container,
                fragment,
                tag
            )
            addToBackStack(tag)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        sharedViewModel.selectedItemId = navigationView.selectedItemId
//        outState.putInt(SELECTED_ITEM_ID, navigationView.selectedItemId)
    }

    private fun drawMenu() {
        for (section in sharedViewModel.menuData.sectionsData) {
            navigationView.menu.add(section.groupId, section.itemId, section.order, section.title)
                .setIcon(section.iconRes)
        }
    }
}