package com.shpp.eorlov.assignment1.ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shpp.eorlov.assignment1.databinding.DialogFragmentLoadImageBinding
import com.shpp.eorlov.assignment1.ui.dialogfragment.adapter.ImageLoaderRecyclerAdapter
import com.shpp.eorlov.assignment1.ui.mycontacts.MyContactsViewModel
import com.shpp.eorlov.assignment1.validator.Validator
import java.util.*
import javax.inject.Inject

class ImageLoaderDialogFragment : DialogFragment() {

    @Inject
    lateinit var validator: Validator

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: MyContactsViewModel by viewModels()
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

    }

    private fun setListeners() {
        TODO("Not yet implemented")
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