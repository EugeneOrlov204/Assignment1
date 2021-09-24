package com.shpp.eorlov.assignment1.ui.image_loader_dialog_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageLoaderViewModel @Inject constructor(
    private val contactsDatabase: ContactsDatabaseImpl
) : ViewModel() {

    val imagesListLiveData = MutableLiveData<MutableList<String>>(ArrayList())

    fun initializeData() {
        val data = contactsDatabase.listOfImages.toMutableList()
        if (data.isNotEmpty()) {
            imagesListLiveData.value = data
        }
    }
}
