package com.shpp.eorlov.assignment1.ui.contactProfile

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentContactProfileBinding
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactProfileFragment : BaseFragment() {

    private val args: ContactProfileFragmentArgs by navArgs()

    private lateinit var binding: FragmentContactProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.explode)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    private fun setListeners() {
        binding.apply {
            imageButtonExit.clickWithDebounce {
                activity?.onBackPressed()
            }
            buttonAddToMyContacts.clickWithDebounce {
                args.users.add(args.userModel)
                activity?.onBackPressed()
            }
        }
    }

    private fun initViews() {
        args.userModel.apply {
            binding.draweeViewUserImage.setImageURI(photo)
            binding.textViewUserName.text = name
            binding.textViewUserProfession.text = career
            binding.textViewUserResidence.text = address
        }
    }
}