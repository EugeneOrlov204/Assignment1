package com.shpp.eorlov.assignment1.ui.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shpp.eorlov.assignment1.ui.mycontacts.MyContactsFragment
import com.shpp.eorlov.assignment1.ui.myprofile.MyProfileFragment
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.AMOUNT_OF_VIEWPAGER_ITEMS

class ContactCollectionAdapter(fragment: Fragment, private val email: String) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = AMOUNT_OF_VIEWPAGER_ITEMS

    override fun createFragment(position: Int): Fragment {
        return when(position) {

            ViewPagerItems.PROFILE.position -> {
                val myProfileFragment = MyProfileFragment()
                val arguments = Bundle()
                arguments.putString(Constants.PROFILE_LOGIN, email)
                myProfileFragment.arguments = arguments
                myProfileFragment
            }

            else -> {
                MyContactsFragment()
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