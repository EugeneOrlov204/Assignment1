package com.shpp.eorlov.assignment1.ui.myprofile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.FragmentMyProfileBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.ui.viewpager.CollectionContactFragmentDirections
import com.shpp.eorlov.assignment1.utils.ext.loadImage
import javax.inject.Inject

class MyProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewUserImageMyProfile.loadImage(R.mipmap.ic_launcher)

        userModel = UserModel(
            binding.textViewUserNameMyProfile.text.toString(),
            binding.textViewUserProfessionMyProfile.text.toString(),
            Uri.parse(
                "android.resource://com.shpp.eorlov.assignment1.ui.myprofile/" + binding.imageViewUserImageMyProfile.drawable
            ).toString(),
            binding.textViewPersonResidence.text.toString(),
            "",
            "",
            ""
        )
        setListeners()
        restoreUIElementsLogic()

        setNameOfPerson(name = activity?.intent?.getStringExtra("personName").toString())
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

    /**
     * Restores UI elements states.
     * For example a button has become enable
     */
    private fun restoreUIElementsLogic() {
        binding.buttonEditProfile.isEnabled = true
    }

    /**
     * Set parsed intent's string to title of person's image
     */
    private fun setNameOfPerson(name: String) {
        val messageText = binding.textViewUserNameMyProfile
        messageText.text = name
    }
}