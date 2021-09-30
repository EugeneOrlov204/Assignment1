package com.shpp.eorlov.assignment1.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shpp.eorlov.assignment1.ui.myContacts.MyContactsFragment
import com.shpp.eorlov.assignment1.ui.myprofile.MyProfileFragment
import com.shpp.eorlov.assignment1.utils.Constants.AMOUNT_OF_VIEWPAGER_ITEMS

class ContactCollectionAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = AMOUNT_OF_VIEWPAGER_ITEMS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            ViewPagerItems.PROFILE.position -> {
                MyProfileFragment()
            }
            ViewPagerItems.LIST.position -> {
                MyContactsFragment()
            }
            else -> {
                throw Exception("Wrong position of ContactCollectionAdapter: $position")
            }
        }
    }

    enum class ViewPagerItems {
        PROFILE {
            override val position = 0
        },
        LIST {
            override val position = 1
        };

        abstract val position: Int
    }
}