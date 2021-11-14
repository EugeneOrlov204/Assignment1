package com.shpp.eorlov.assignment1.ui.imageLoaderDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.model.PhotoModel
import com.shpp.eorlov.assignment1.repository.PhotoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageLoaderViewModel @Inject constructor(
    private val photoRepository: PhotoRepositoryImpl
) : ViewModel() {

    val imagesListLiveData = MutableLiveData<MutableList<String>>(ArrayList())

    init {
//        viewModelScope.launch {
//            photoRepository.clearTable()
//        }
        imagesListLiveData.value = loadDefaultImage()
        getAllPhotos()
    }

    fun addImage(imagePath: String) {
        imagesListLiveData.value?.add(index = 1, imagePath)

        viewModelScope.launch {
            photoRepository.insertAll(PhotoModel(photoPath = imagePath))
        }

        imagesListLiveData.value = imagesListLiveData.value
    }

    private fun getAllPhotos() {
        viewModelScope.launch {
            val listOfPhotoModels = photoRepository.getAllPhotos()

            imagesListLiveData.value?.addAll(listOfPhotoModels.map { photoModel ->
                photoModel.photoPath
            }.reversed().toMutableList())
            imagesListLiveData.value = imagesListLiveData.value
        }
    }

    private fun loadDefaultImage(): MutableList<String> {
        return mutableListOf("")
    }

    fun getItem(position: Int): String? {
        return imagesListLiveData.value?.get(position)
    }

    fun removeItem(removedItem: String) {
        viewModelScope.launch {
            photoRepository.removeItem(PhotoModel(photoPath = removedItem))
        }
        imagesListLiveData.value?.remove(removedItem)
        imagesListLiveData.value = imagesListLiveData.value
    }
}
