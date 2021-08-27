package com.shpp.eorlov.assignment1.ui.editprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import javax.inject.Inject
import kotlin.math.abs

class EditProfileFragment : Fragment() {


    @Inject
    @field:SharedPrefStorage
    lateinit var storage: Storage

    //todo add validation to edit texts' fields
    //todo remove ALL hardcoded data in dialog fragment and edit profile fields

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        restoreInputFields()
        setListeners()
        return binding.root
    }

    private fun restoreInputFields() {
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
        binding.buttonSave.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                saveUserData()
                activity?.onBackPressed()
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }

        binding.imageViewImageLoader.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                loadImageFromGallery()
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
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