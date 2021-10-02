package com.shpp.eorlov.assignment1.ui.details

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentDetailViewBinding
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailViewFragment : BaseFragment() {


    private val args: DetailViewFragmentArgs by navArgs()
    private val viewModel: DetailViewViewModel by viewModels()


    private lateinit var binding: FragmentDetailViewBinding


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
        binding = FragmentDetailViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initializeData(args.contact)
        initViews()
        setListeners()
        setObservers()
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

    private fun setObservers() {
        viewModel.apply {

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
    }

    private fun setListeners() {

        binding.imageButtonContactDialogCloseButton.clickWithDebounce {
            activity?.onBackPressed()
        }
    }

    private fun initViews() {
        viewModel.userLiveData.value?.apply {
            binding.draweeViewUserImageDetailView.setImageURI(photo)
            binding.textViewUserNameDetailView.text = name
            binding.textViewUserProfessionDetailView.text = career
            binding.textViewUserResidenceDetailView.text = address
        }
    }
}
