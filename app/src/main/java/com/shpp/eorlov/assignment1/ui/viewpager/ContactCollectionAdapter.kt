package com.shpp.eorlov.assignment1.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ContactCollectionAdapter(fragment: Fragment, private val fragsListHere: List<Fragment>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragsListHere.size

    override fun createFragment(position: Int): Fragment {
        return fragsListHere[position]
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