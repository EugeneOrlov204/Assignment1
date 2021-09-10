package com.shpp.eorlov.assignment1.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabaseImplementation
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.storage.SharedPreferencesStorage
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.validator.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewViewModel @Inject constructor() : ViewModel() {

    val userLiveData: MutableLiveData<UserModel> by lazy(LazyThreadSafetyMode.NONE)  {
        MutableLiveData(contactsDatabase.getDefaultUserModel())
    }

    val loadEvent = MutableLiveData<Results>()

    @Inject
    lateinit var storage: SharedPreferencesStorage

    @Inject
    lateinit var contactsDatabase: ContactsDatabaseImplementation

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