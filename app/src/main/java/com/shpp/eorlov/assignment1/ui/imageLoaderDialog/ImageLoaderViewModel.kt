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
        val data = loadImages()
        if (data.isNotEmpty()) {
            imagesListLiveData.value = data
        }
    }

    fun addImage(imagePath: String) {
        imagesListLiveData.value?.add(0, imagePath)
        imagesListLiveData.value = imagesListLiveData.value
    }

    private fun loadImages(): MutableList<String> {
        val urlOfPhoto = Constants.DEFAULT_PATH_TO_IMAGE
        val result = mutableListOf<String>()

        //todo remove hardcoded data
//        for (i in 0..9) {
//            result.add(urlOfPhoto + i)
//        }

        return result
    }
}
