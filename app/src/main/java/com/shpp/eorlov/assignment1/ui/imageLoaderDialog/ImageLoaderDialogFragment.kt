package com.shpp.eorlov.assignment1.ui.imageLoaderDialog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shpp.eorlov.assignment1.databinding.DialogFragmentLoadImageBinding
import com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter.ImageLoaderListAdapter
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageLoaderDialogFragment : DialogFragment() {

    private val viewModel: ImageLoaderViewModel by viewModels()
    private val imageLoaderAdapter: ImageLoaderListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ImageLoaderListAdapter()
    }
    private val TAG = "ImageLoaderDialogFragment"

    private lateinit var binding: DialogFragmentLoadImageBinding

    private var pathToLoadedImageFromGallery: String = ""
    private var imageLoaderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val imageData = result.data?.data ?: return@registerForActivityResult
                pathToLoadedImageFromGallery = imageData.toString()
                viewModel.addImage(pathToLoadedImageFromGallery)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentLoadImageBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        setListeners()
        setObservers()
    }

    override fun onStart() {
        super.onStart()

        //Set size of DialogFragment to all size of parent
        if (dialog != null && dialog?.window != null) {
            val params: ViewGroup.LayoutParams =
                dialog?.window?.attributes ?: return
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog?.window?.attributes = params as WindowManager.LayoutParams
        }
    }

    private fun setObservers() {

        postponeEnterTransition()

        viewModel.apply {
            imagesListLiveData.observe(viewLifecycleOwner) { list ->
                imageLoaderAdapter.submitList(list.toMutableList())

                // Start the transition once all views have been
                // measured and laid out
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }
        }
    }

    private fun setListeners() {
//        binding.recyclerViewImageLoader[0].clickWithDebounce {
//            loadImageFromGallery()
//        }

        binding.textViewOpenGallery.clickWithDebounce {
            loadImageFromGallery()
        }

        binding.textViewDeleteCurrentPhoto.clickWithDebounce {
            Log.d(TAG, "Photo has been deleted")
        }

        binding.textViewCancel.clickWithDebounce {
            dismiss()
        }
    }

    private fun loadImageFromGallery() {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        imageLoaderLauncher.launch(gallery)
    }

    private fun initRecycler() {

        binding.recyclerViewImageLoader.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = imageLoaderAdapter
        }
    }
}