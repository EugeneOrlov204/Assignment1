package com.shpp.eorlov.assignment1.ui.image_loader_dialog_fragment

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shpp.eorlov.assignment1.databinding.DialogFragmentLoadImageBinding
import com.shpp.eorlov.assignment1.ui.image_loader_dialog_fragment.adapter.ImageLoaderRecyclerAdapter
import com.shpp.eorlov.assignment1.validator.Validator
import javax.inject.Inject

class ImageLoaderDialogFragment : DialogFragment() {

    @Inject
    lateinit var validator: Validator

    private val viewModel: ImageLoaderViewModel by viewModels()
    private val imageLoaderRecyclerAdapter: ImageLoaderRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ImageLoaderRecyclerAdapter()
    }

    private lateinit var dialogBinding: DialogFragmentLoadImageBinding

    private var pathToLoadedImageFromGallery: String = ""

    //fixme fix bug with loading image from gallery
//    private var imageLoaderLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            val imageView: AppCompatImageView = dialogBinding.recyclerViewImageLoader.
//            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
//                val imageData = result.data?.data ?: return@registerForActivityResult
//                pathToLoadedImageFromGallery = imageData.toString()
//                imageView.loadImage(imageData)
//            }
//        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogBinding = DialogFragmentLoadImageBinding.inflate(LayoutInflater.from(context))
        return dialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        setListeners()
        setObservers()
    }

    private fun setObservers() {

        postponeEnterTransition()

        viewModel.apply {
            imagesListLiveData.observe(viewLifecycleOwner) { list ->
                imageLoaderRecyclerAdapter.submitList(list.toMutableList())

                // Start the transition once all views have been
                // measured and laid out
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }

//            loadEvent.apply {
//                observe(viewLifecycleOwner) { event ->
//                    when (event) {
//                        Results.OK -> {
//                            unlockUI()
//                            binding.contentLoadingProgressBar.isVisible = false
//                        }
//
//                        Results.LOADING -> {
//                            lockUI()
//                            binding.contentLoadingProgressBar.isVisible = true
//                        }
//
//                        Results.INITIALIZE_DATA_ERROR -> {
//                            unlockUI()
//                            binding.contentLoadingProgressBar.isVisible = false
//                            Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG).show()
//                        }
//                        else -> {
//                        }
//                    }
//
//                }
//            }
        }
    }

    private fun setListeners() {
        //todo "Not yet implemented"
    }


    private fun initRecycler() {

        dialogBinding.recyclerViewImageLoader.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = imageLoaderRecyclerAdapter
        }
    }

    private fun loadImageFromGallery() {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
//   fixme     imageLoaderLauncher.launch(gallery)
    }
}