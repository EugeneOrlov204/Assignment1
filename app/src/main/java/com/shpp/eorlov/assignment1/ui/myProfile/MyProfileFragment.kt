package com.shpp.eorlov.assignment1.ui.myProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentMyProfileBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.editProfile.EditProfileFragment
import com.shpp.eorlov.assignment1.ui.viewPager.CollectionContactFragment
import com.shpp.eorlov.assignment1.ui.viewPager.CollectionContactFragmentDirections
import com.shpp.eorlov.assignment1.ui.viewPager.ContactCollectionAdapter
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.FeatureNavigationEnabled
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.TransitionKeys.USER_MODEL_KEY
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint

//todo implement log out button
@AndroidEntryPoint
class MyProfileFragment : BaseFragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: MyProfileViewModel by viewModels()

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var userModel: UserModel

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
        sharedViewModel.updatedUserLiveData.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.updateProfile(it)
            }
        }
    }

    private fun setViewModelObservers() {
        setUserLiveDataObserver()
        setLoadEventObserver()
    }


    private fun setUserLiveDataObserver() {
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                userModel = it
                updateProfile()
            }
            viewModel.loadEventLiveData.value = Results.OK
        }
    }

    private fun setLoadEventObserver() {
        viewModel.loadEventLiveData.apply {
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

    private fun updateProfile() {
        binding.apply {
            textViewUserNameMyProfile.text = userModel.name
            textViewUserProfessionMyProfile.text = userModel.career
            textViewPersonResidence.text = userModel.address
            simpleDraweeViewUserImageMyProfile.setImageURI(userModel.image ?: "")
            textViewGoToSettingsAndFillOutTheProfile.isVisible = !viewModel.isProfileFilledOut()
        }
    }


    private fun setListeners() {
        binding.buttonEditProfile.setOnClickListener {
            if(FeatureNavigationEnabled.featureNavigationEnabled) {
                val action =
                    CollectionContactFragmentDirections.actionCollectionContactFragmentToEditProfileFragment(
                        userModel
                    )
                findNavController().navigate(action)
            } else {
                val fragmentManager = activity?.supportFragmentManager

                val arguments = Bundle().apply {
                    putParcelable(USER_MODEL_KEY, userModel)
                }

                fragmentManager?.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fragmentContainerView, EditProfileFragment().apply {
                        this.arguments = arguments
                    })
                    addToBackStack(null)
                }
            }
        }

        binding.buttonViewMyContacts.clickWithDebounce {
            (parentFragment as CollectionContactFragment).viewPager.currentItem =
                ContactCollectionAdapter.ViewPagerItems.LIST.position
        }

        binding.textViewLogOutMyProfile.clickWithDebounce {
           val action = CollectionContactFragmentDirections.actionCollectionContactFragmentToSignInFragment()
            findNavController().navigate(action)
        }
    }
}
