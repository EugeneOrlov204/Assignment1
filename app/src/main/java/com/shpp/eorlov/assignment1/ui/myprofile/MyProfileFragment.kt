package com.shpp.eorlov.assignment1.ui.myprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.FragmentMyProfileBinding
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.ui.auth.AuthActivity
import com.shpp.eorlov.assignment1.utils.ext.loadImage

class MyProfileFragment : Fragment() {
    private lateinit var binding: FragmentMyProfileBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).contactComponent.inject(this)
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
        binding.imageViewPersonImage.loadImage(R.mipmap.ic_launcher)
        restoreUIElementsLogic()

        setNameOfPerson(name = activity?.intent?.getStringExtra("personName").toString())

        binding.buttonEditProfile.setOnClickListener {
            goToAuthActivity()
        }
    }

    /**
     * Restores UI elements states.
     * For example button the button has become enable
     */
    private fun restoreUIElementsLogic() {
        binding.buttonEditProfile.isEnabled = true
    }

    /**
     * Set parsed intent's string to title of person's image
     */
    private fun setNameOfPerson(name: String) {
        val messageText = binding.textViewPersonName
        messageText.text = name
    }

    /**
     * Change current activity to AuthActivity
     */
    private fun goToAuthActivity() {
        binding.buttonEditProfile.isEnabled = false
        startActivity(Intent(activity, AuthActivity::class.java))
        activity?.onBackPressed()
    }
}