package com.shpp.eorlov.assignment1.ui.image_loader_dialog_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabaseImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageLoaderViewModel @Inject constructor() : ViewModel() {

    val imagesListLiveData = MutableLiveData<MutableList<String>>(ArrayList())

    @Inject
    lateinit var contactsDatabase: ContactsDatabaseImplementation

    fun initializeData() {
        val data = contactsDatabase.listOfImages.toMutableList()
        if (data.isNotEmpty()) {
            imagesListLiveData.value = data
        }
    }
}
