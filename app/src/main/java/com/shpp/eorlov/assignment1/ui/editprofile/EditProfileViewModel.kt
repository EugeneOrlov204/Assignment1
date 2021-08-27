package com.shpp.eorlov.assignment1.ui.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.Results
import javax.inject.Inject


class EditProfileViewModel @Inject constructor() : ViewModel() {

    val userLiveData = MutableLiveData<UserModel>()
    val loadEvent = MutableLiveData<Results>()

    @Inject
    lateinit var contactsDatabase: ContactsDatabase

    fun initializeData() {
        if (userLiveData.value == null) {
            loadEvent.value = Results.INITIALIZE_DATA_ERROR
        } else {
            loadEvent.value = Results.LOADING
            val data = contactsDatabase.getDefaultUserModel()

            userLiveData.value = data
            loadEvent.value = Results.OK
        }
    }

}