package com.shpp.eorlov.assignment1.ui.editprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shpp.eorlov.assignment1.databinding.FragmentEditProfileBinding
import com.shpp.eorlov.assignment1.di.SharedPrefStorage
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.storage.Storage
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.MY_PROFILE_BIRTHDATE_KEY
import com.shpp.eorlov.assignment1.utils.Constants.MY_PROFILE_EMAIL_KEY
import com.shpp.eorlov.assignment1.utils.Constants.MY_PROFILE_NAME_KEY
import com.shpp.eorlov.assignment1.utils.Constants.MY_PROFILE_PHONE_KEY
import com.shpp.eorlov.assignment1.utils.Constants.MY_PROFILE_PHOTO_KEY
import com.shpp.eorlov.assignment1.utils.Constants.MY_PROFILE_PROFESSION_KEY
import com.shpp.eorlov.assignment1.utils.Constants.MY_PROFILE_RESIDENCE_KEY
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import javax.inject.Inject
import kotlin.math.abs

class EditProfileFragment : Fragment() {

    //todo add validation to edit texts' fields
    //todo remove ALL hardcoded data in dialog fragment and edit profile fields

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @field:SharedPrefStorage
    lateinit var storage: Storage

    private lateinit var viewModel: EditProfileViewModel
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userModel: UserModel

    private var pathToLoadedImageFromGallery: String = ""
    private var imageLoaderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageView: AppCompatImageView = binding.imageViewPersonPhoto
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val imageData = result.data?.data ?: return@registerForActivityResult
                pathToLoadedImageFromGallery = imageData.toString()
                imageView.loadImage(imageData)
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).contactComponent.inject(this)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[EditProfileViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeData()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.apply {
            userLiveData.observe(viewLifecycleOwner) { userModel ->

            }

            loadEvent.apply {
                observe(viewLifecycleOwner) { event ->
                    when (event) {
                        Results.OK -> {
                            unlockUI()
                            binding.contentLoadingProgressBarRecyclerView.isVisible = false
                        }

                        Results.LOADING -> {
                            lockUI()
                            binding.contentLoadingProgressBarRecyclerView.isVisible = true
                        }

                        Results.INITIALIZE_DATA_ERROR -> {
                            unlockUI()
                            binding.contentLoadingProgressBarRecyclerView.isVisible = false
                            Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }

                }
            }
        }
    }

    //todo move to another class
    private fun lockUI() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun unlockUI() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    private fun initializeData() {

        viewModel.initializeData()

        //todo move to view model
        userModel = UserModel(
            name = storage.getString(MY_PROFILE_NAME_KEY) ?: "",
            profession = storage.getString(MY_PROFILE_PROFESSION_KEY) ?: "",
            photo = storage.getString(MY_PROFILE_PHOTO_KEY) ?: "",
            residenceAddress = storage.getString(MY_PROFILE_RESIDENCE_KEY) ?: "",
            birthDate = storage.getString(MY_PROFILE_BIRTHDATE_KEY) ?: "",
            phoneNumber = storage.getString(MY_PROFILE_PHONE_KEY) ?: "",
            email = storage.getString(MY_PROFILE_EMAIL_KEY) ?: ""
        )

        pathToLoadedImageFromGallery = userModel.photo

        binding.apply {
            textInputEditTextUsername.setText(userModel.name)
            textInputEditTextCareer.setText(userModel.profession)
            textInputEditTextAddress.setText(userModel.residenceAddress)
            textInputEditTextBirthdate.setText(userModel.birthDate)
            textInputEditTextPhone.setText(userModel.phoneNumber)
            textInputEditTextEmail.setText(userModel.email)
            imageViewPersonPhoto.loadImage(userModel.photo)
        }
    }

    private var previousClickTimestamp = SystemClock.uptimeMillis()

    private fun setListeners() {
       binding.apply {
           buttonSave.setOnClickListener {
               if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                   saveUserData()
                   activity?.onBackPressed()
                   previousClickTimestamp = SystemClock.uptimeMillis()
               }
           }

           imageViewImageLoader.setOnClickListener {
               if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                   loadImageFromGallery()
                   previousClickTimestamp = SystemClock.uptimeMillis()
               }
           }

           imageButtonContactDialogCloseButton.setOnClickListener {
               if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                   activity?.onBackPressed()
               }
           }

//         textInputEditTextUsername.onChange
//         textInputEditTextCareer
//        imageViewPersonPhoto
//          textInputEditTextAddress
//           textInputEditTextBirthdate
//          textInputEditTextPhone
//          textInputEditTextEmail
       }
    }


    private fun loadImageFromGallery() {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        imageLoaderLauncher.launch(gallery)
    }

    //fixme save only changed fields
    private fun saveUserData() {
        binding.apply {
            storage.save(MY_PROFILE_NAME_KEY, textInputEditTextUsername.text.toString())
            storage.save(MY_PROFILE_PROFESSION_KEY, textInputEditTextCareer.text.toString())
            storage.save(MY_PROFILE_PHOTO_KEY, pathToLoadedImageFromGallery)
            storage.save(MY_PROFILE_RESIDENCE_KEY, textInputEditTextAddress.text.toString())
            storage.save(MY_PROFILE_BIRTHDATE_KEY, textInputEditTextBirthdate.text.toString())
            storage.save(MY_PROFILE_PHONE_KEY, textInputEditTextPhone.text.toString())
            storage.save(MY_PROFILE_EMAIL_KEY, textInputEditTextEmail.text.toString())
        }
    }

}