package com.shpp.eorlov.assignment1.ui.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentViewPagerBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.MyContactsFragment
import com.shpp.eorlov.assignment1.ui.myprofile.MyProfileFragment
import com.shpp.eorlov.assignment1.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionContactFragment : BaseFragment() {

    lateinit var viewPager: ViewPager2

    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var contactCollectionAdapter: ContactCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    private fun initAdapter() {
        viewPager = binding.pager

        //fixme get userModel from internet
        contactCollectionAdapter = ContactCollectionAdapter(
            this, UserModel(
                "", "", "", "", "", "",""
            )
        )
        viewPager.adapter = contactCollectionAdapter
        viewPager.currentItem = ContactCollectionAdapter.ViewPagerItems.PROFILE.position
    }
}