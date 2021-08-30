package com.shpp.eorlov.assignment1.ui.myprofile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentMyProfileBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.viewpager.CollectionContactFragmentArgs
import com.shpp.eorlov.assignment1.ui.viewpager.CollectionContactFragmentDirections
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import javax.inject.Inject

class MyProfileFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var viewModel: MyProfileViewModel
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var userModel: UserModel
    private lateinit var login: String

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).contactComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MyProfileViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

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
        login = arguments?.getString(Constants.PROFILE_LOGIN) ?: ""
        initializeProfile()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.apply {

            userLiveData.observe(viewLifecycleOwner) {
                updateProfile()
                viewModel.saveData(login)
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

        sharedViewModel.apply {
            updatedUser.observe(viewLifecycleOwner) { list ->
                list?.let {
                    viewModel.updateProfile(list)
                }
            }
        }
    }

    private fun initializeProfile() {
        viewModel.initializeData(login)

        updateProfile()
    }

    private fun updateProfile() {
        userModel = viewModel.userLiveData.value ?: return

        binding.apply {
            textViewUserNameMyProfile.text = userModel.name
            textViewUserProfessionMyProfile.text = userModel.profession
            textViewPersonResidence.text = userModel.residenceAddress
            imageViewUserImageMyProfile.loadImage(userModel.photo)
            textViewGoToSettingsAndFillOutTheProfile.isVisible = !viewModel.isProfileFilledOut()
        }
    }


    private fun setListeners() {
        binding.buttonEditProfile.setOnClickListener {
            val action =
                CollectionContactFragmentDirections.actionCollectionContactFragmentToEditProfileFragment(
                    login
                )
            findNavController().navigate(action)
        }
    }
}