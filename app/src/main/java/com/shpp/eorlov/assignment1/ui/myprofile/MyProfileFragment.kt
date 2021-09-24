package com.shpp.eorlov.assignment1.ui.myprofile

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

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
        receivedUserModel = arguments?.getParcelable(Constants.REGISTERED_USER_MODEL_KEY)
            ?: throw Exception("Received user model is null!")
        initializeProfile()
        setListeners()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    private fun setObservers() {
        viewModel.apply {

            userLiveData.observe(viewLifecycleOwner) {
                updateProfile()
                viewModel.saveData(receivedUserModel.email ?: "")
            }

            loadEvent.apply {
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

        sharedViewModel.updatedUser.observe(viewLifecycleOwner) { list ->
            list?.let {
                viewModel.updateProfile(list)
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
            textViewUserProfessionMyProfile.text = userModel.profession
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