package com.shpp.eorlov.assignment1.ui.myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentMyProfileBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.viewpager.CollectionContactFragment
import com.shpp.eorlov.assignment1.ui.viewpager.CollectionContactFragmentDirections
import com.shpp.eorlov.assignment1.ui.viewpager.ContactCollectionAdapter
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint

import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.utils.ext.loadImage


@AndroidEntryPoint
class MyProfileFragment : BaseFragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: MyProfileViewModel by viewModels()

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var userModel: UserModel
    private lateinit var receivedUserModel: UserModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getUser(viewModel.fetchToken())
        setListeners()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAreYouSureDialog()
            }
        })
    }

    private fun showAreYouSureDialog() {
        Snackbar.make(
            binding.root,
            getString(R.string.are_you_sure),
            Constants.SNACKBAR_DURATION
        ).setAction("Yes") {
            requireActivity().finish()
        }.show()
    }

    private fun setObservers() {
        setViewModelObservers()
        setSharedViewModelObservers()
    }

    private fun setSharedViewModelObservers() {
        sharedViewModel.updatedUser.observe(viewLifecycleOwner) { userModel ->
            userModel?.let {
                viewModel.updateProfile(userModel)
            }
        }
        sharedViewModel.getUser.observe(viewLifecycleOwner) { userModel ->
            userModel?.let {
                receivedUserModel = userModel.data.user
                initializeProfile()
            }

        }
    }

    private fun setViewModelObservers() {
        setUserLiveDataObserver()
        setLoadEventObserver()
    }

    private fun setLoadEventObserver() {
        viewModel.loadEvent.apply {
            observe(viewLifecycleOwner) { event ->
                when (event) {
                    Results.OK -> {
                        unlockUI()
                        binding.contentLoadingProgressBar.isVisible = false
                    }
                    Results.LOADING -> {
                        lockUI()
                        binding.contentLoadingProgressBar.isVisible = true
                    }

                    Results.INITIALIZE_DATA_ERROR -> {
                        unlockUI()
                        binding.contentLoadingProgressBar.isVisible = false
                        Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun setUserLiveDataObserver() {
        viewModel.userLiveData.observe(viewLifecycleOwner) { userModel ->
            if (userModel != null) {
                receivedUserModel = userModel
                updateProfile()
                viewModel.saveData(receivedUserModel.email ?: "")
            }
        }
    }

    private fun initializeProfile() {
        viewModel.initializeData(receivedUserModel)

        updateProfile()
    }

    private fun updateProfile() {
        userModel = viewModel.userLiveData.value ?: return

        binding.apply {
            textViewUserNameMyProfile.text = userModel.name
            textViewUserProfessionMyProfile.text = userModel.career
            textViewPersonResidence.text = userModel.residenceAddress
            imageViewUserImageMyProfile.loadImage(userModel.photo ?: "")
            textViewGoToSettingsAndFillOutTheProfile.isVisible = !viewModel.isProfileFilledOut()
        }
    }


    private fun setListeners() {
        binding.buttonEditProfile.setOnClickListener {
            val action =
                CollectionContactFragmentDirections.actionCollectionContactFragmentToEditProfileFragment(
                    userModel
                )
            findNavController().navigate(action)
        }

        binding.buttonViewMyContacts.clickWithDebounce {
            (parentFragment as CollectionContactFragment).viewPager.currentItem =
                ContactCollectionAdapter.ViewPagerItems.LIST.position
        }
    }
}
