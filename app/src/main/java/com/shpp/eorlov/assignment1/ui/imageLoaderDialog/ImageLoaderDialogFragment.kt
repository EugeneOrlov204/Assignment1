package com.shpp.eorlov.assignment1.ui.imageLoaderDialog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.DialogFragmentLoadImageBinding
import com.shpp.eorlov.assignment1.model.PhotoModel
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter.ImageLoaderListAdapter
import com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter.listeners.ImageClickListener
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ImageLoaderDialogFragment : DialogFragment(), ImageClickListener {

    private val viewModel: ImageLoaderViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val imageLoaderAdapter: ImageLoaderListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ImageLoaderListAdapter(this)
    }

    private var pathToLoadedImageFromGallery: String = ""
    private var imageLoaderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val imageData = result.data?.data ?: return@registerForActivityResult
                pathToLoadedImageFromGallery = imageData.toString()
                viewModel.addImage(pathToLoadedImageFromGallery)
            }
        }
    private var takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.addImage(currentPhotoPath)
                getImage(currentPhotoPath)
            }
        }

    private lateinit var binding: DialogFragmentLoadImageBinding
    private lateinit var currentPhotoPath: String

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

    /**
     * Returns an image from DialogFragment to a Fragment
     */
    override fun getImage(imagePath: String) {
        sharedViewModel.newPhotoLiveData.value = imagePath
        dismiss()
    }

    override fun takePicture() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            try {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also { photo ->
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context ?: return,
                        "com.shpp.eorlov.assignment1.fileprovider",
                        photo
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureLauncher.launch(takePictureIntent)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    /**
     * Returns itemTouchHelperCallBack for recycler view
     */
    private fun getItemTouchHelperCallBack() = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.UP
    ) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            removeItemFromRecyclerView(viewHolder.bindingAdapterPosition)
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            if (viewHolder.bindingAdapterPosition != 0) {
                return makeMovementFlags(0, ItemTouchHelper.UP)
            }
            return makeMovementFlags(0, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }
    }

    private fun removeItemFromRecyclerView(
        position: Int,
    ) {
        val removedItem: String = viewModel.getItem(position) ?: return
        viewModel.removeItem(removedItem)
    }

    private fun setObservers() {
        viewModel.apply {
            imagesListLiveData.observe(viewLifecycleOwner) { list ->
                imageLoaderAdapter.submitList(list.toMutableList())
            }
        }
    }

    private fun setListeners() {

        binding.textViewOpenGallery.clickWithDebounce {
            loadImageFromGallery()
        }

        binding.textViewDeleteCurrentPhoto.clickWithDebounce {
            sharedViewModel.newPhotoLiveData.value = ""
            dismiss()
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

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun initRecycler() {
        binding.recyclerViewImageLoader.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = imageLoaderAdapter

            //Implement swipe-to-delete
            ItemTouchHelper(getItemTouchHelperCallBack()).attachToRecyclerView(this)
        }
    }
}