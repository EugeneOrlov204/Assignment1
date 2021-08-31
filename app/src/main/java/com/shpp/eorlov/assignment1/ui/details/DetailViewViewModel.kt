package com.shpp.eorlov.assignment1.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.di.SharedPrefStorage
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.storage.Storage
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ValidateOperation
import com.shpp.eorlov.assignment1.utils.evaluateErrorMessage
import com.shpp.eorlov.assignment1.validator.Validator
import javax.inject.Inject


class DetailViewViewModel @Inject constructor() : ViewModel() {

    val userLiveData: MutableLiveData<UserModel> by lazy {
        MutableLiveData(contactsDatabase.getDefaultUserModel())
    }

    val loadEvent = MutableLiveData<Results>()

    @Inject
    @field:SharedPrefStorage
    lateinit var storage: Storage

    @Inject
    lateinit var contactsDatabase: ContactsDatabase

    @Inject
    lateinit var validator: Validator

    fun initializeData(userModel: UserModel) {
        if (userLiveData.value == null) {
            loadEvent.value = Results.INITIALIZE_DATA_ERROR
        } else {
            loadEvent.value = Results.LOADING

            userLiveData.value = userModel

            loadEvent.value = Results.OK
        }
    }
}