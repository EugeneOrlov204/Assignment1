package com.shpp.eorlov.assignment1.ui.myprofile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.FragmentMyProfileBinding
import com.shpp.eorlov.assignment1.di.SharedPrefStorage
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.storage.Storage
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.ui.viewpager.CollectionContactFragmentDirections
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import javax.inject.Inject

class MyProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @field:SharedPrefStorage
    lateinit var storage: Storage

    private lateinit var viewModel: MyProfileViewModel
    private lateinit var binding: FragmentMyProfileBinding

    private lateinit var userModel: UserModel //todo Replace with ViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).contactComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MyProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        initializeProfile()
        setListeners()

        return binding.root
    }


    private fun initializeProfile() {
        userModel = UserModel(
            name = storage.getString(Constants.MY_PROFILE_NAME_KEY) ?: "",
            profession = storage.getString(Constants.MY_PROFILE_PROFESSION_KEY) ?: "",
            photo = storage.getString(Constants.MY_PROFILE_PHOTO_KEY) ?: "",
            residenceAddress = storage.getString(Constants.MY_PROFILE_RESIDENCE_KEY) ?: "",
            birthDate = storage.getString(Constants.MY_PROFILE_BIRTHDATE_KEY) ?: "",
            phoneNumber = storage.getString(Constants.MY_PROFILE_PHONE_KEY) ?: "",
            email = storage.getString(Constants.MY_PROFILE_EMAIL_KEY) ?: ""
        )

        binding.apply {
            textViewUserNameMyProfile.text = userModel.name
            textViewUserProfessionMyProfile.text = userModel.profession
            textViewPersonResidence.text = userModel.residenceAddress
            imageViewUserImageMyProfile.loadImage(userModel.photo)
            textViewGoToSettingsAndFillOutTheProfile.isVisible = !profileIsFilledOut()
        }
    }

    /**
     * Returns true if all field of profile in
     * fragment_edit_profile is filled out,
     * otherwise false
     */
    private fun profileIsFilledOut(): Boolean {
        return userModel.birthDate.isNotEmpty() &&
                userModel.email.isNotEmpty() &&
                userModel.name.isNotEmpty() &&
                userModel.phoneNumber.isNotEmpty() &&
                userModel.photo.isNotEmpty() &&
                userModel.profession.isNotEmpty() &&
                userModel.residenceAddress.isNotEmpty()
    }

    private fun setListeners() {
        binding.buttonEditProfile.setOnClickListener {
            val action =
                CollectionContactFragmentDirections.actionCollectionContactFragmentToEditProfileFragment(
                    userModel
                )
            findNavController().navigate(action)
        }
    }
}