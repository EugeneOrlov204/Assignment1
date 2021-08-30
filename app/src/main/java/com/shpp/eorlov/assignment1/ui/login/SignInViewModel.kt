package com.shpp.eorlov.assignment1.ui.login


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.di.ContactsDatabaseStorage
import com.shpp.eorlov.assignment1.di.SharedPrefStorage
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.storage.Storage
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ValidateOperation
import com.shpp.eorlov.assignment1.utils.evaluateErrorMessage
import com.shpp.eorlov.assignment1.validator.Validator
import javax.inject.Inject


class SignInViewModel @Inject constructor() : ViewModel() {

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

    fun initializeData() {
        if (userLiveData.value == null) {
            loadEvent.value = Results.INITIALIZE_DATA_ERROR
        } else {
            loadEvent.value = Results.LOADING

            val data = contactsDatabase.getUserModelFromStorage()
            userLiveData.value = data

            loadEvent.value = Results.OK
        }
    }

    fun getLogin() = storage.getString(Constants.PROFILE_LOGIN)

    fun getPassword() = storage.getString(Constants.PROFILE_PASSWORD)

    fun saveLoginData(login: String, password: String) {
        storage.save(Constants.PROFILE_LOGIN, login)
        storage.save(Constants.PROFILE_PASSWORD, password)
    }
}