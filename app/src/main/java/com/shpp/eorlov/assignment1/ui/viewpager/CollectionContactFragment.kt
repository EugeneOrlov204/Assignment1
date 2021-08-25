package com.shpp.eorlov.assignment1.ui.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.shpp.eorlov.assignment1.databinding.FragmentViewPagerBinding
import com.shpp.eorlov.assignment1.ui.mycontacts.MyContactsFragment
import com.shpp.eorlov.assignment1.ui.myprofile.MyProfileFragment

class CollectionContactFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var contactCollectionAdapter: ContactCollectionAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.pager
        contactCollectionAdapter = ContactCollectionAdapter(this)
        val fragsList = listOf(MyProfileFragment(), MyContactsFragment())
        contactCollectionAdapter.fragsListHere.addAll(fragsList)
        viewPager.adapter = contactCollectionAdapter
        viewPager.currentItem = 0
    }
}