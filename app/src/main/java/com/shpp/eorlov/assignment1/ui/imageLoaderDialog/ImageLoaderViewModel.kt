package com.shpp.eorlov.assignment1.ui.imageLoaderDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageLoaderViewModel @Inject constructor() : ViewModel() {

    val imagesListLiveData = MutableLiveData<MutableList<String>>(ArrayList())

    init {
        imagesListLiveData.value = loadDefaultImage()
    }

    fun addImage(imagePath: String) {
        imagesListLiveData.value?.add(1, imagePath)
        imagesListLiveData.value = imagesListLiveData.value
    }

    private fun loadDefaultImage(): MutableList<String> {
        return mutableListOf("")
    }
}
