package com.shpp.eorlov.assignment1.ui.details

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentDetailViewBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.FeatureNavigationEnabled
import com.shpp.eorlov.assignment1.utils.TransitionKeys
import com.shpp.eorlov.assignment1.utils.TransitionKeys.CONTACT_KEY
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailViewFragment : BaseFragment() {

    private val args: DetailViewFragmentArgs by navArgs()


    private lateinit var binding: FragmentDetailViewBinding
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.explode)

        userModel = if (FeatureNavigationEnabled.featureNavigationEnabled) {
            args.contact
        } else {
            requireArguments().getParcelable(CONTACT_KEY) ?: return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setListeners()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        printLog("On resume")
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }


    private fun setListeners() {
        binding.imageButtonContactDialogCloseButton.clickWithDebounce {
            activity?.onBackPressed()
        }
    }

    private fun initViews() {
        args.contact.apply {
            binding.draweeViewUserImageDetailView.setImageURI(image)
            binding.textViewUserNameDetailView.text = name
            binding.textViewUserProfessionDetailView.text = career
            binding.textViewUserResidenceDetailView.text = address
        }
    }
}
