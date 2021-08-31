package com.shpp.eorlov.assignment1.ui.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentViewPagerBinding
import com.shpp.eorlov.assignment1.ui.mycontacts.MyContactsFragment
import com.shpp.eorlov.assignment1.ui.myprofile.MyProfileFragment
import com.shpp.eorlov.assignment1.utils.Constants

class CollectionContactFragment : BaseFragment() {

    private val args: CollectionContactFragmentArgs by navArgs()

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
        viewPager = binding.pager

        val myProfileFragment = MyProfileFragment()
        val arguments = Bundle()
        arguments.putString(Constants.PROFILE_LOGIN, args.email)
        myProfileFragment.arguments = arguments

        val fragsList = listOf(myProfileFragment, MyContactsFragment())

        contactCollectionAdapter = ContactCollectionAdapter(this, fragsList)

        viewPager.adapter = contactCollectionAdapter
        viewPager.currentItem = ContactCollectionAdapter.ViewPagerItems.PROFILE.position
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }
}