package com.shpp.eorlov.assignment1.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ContactCollectionAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    val fragsListHere = mutableListOf<Fragment>()

    override fun getItemCount(): Int = fragsListHere.size

    override fun createFragment(position: Int): Fragment {
        return fragsListHere[position]
    }
}