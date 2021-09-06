package com.shpp.eorlov.assignment1.ui.signupextended


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.storage.SharedPreferencesStorageImplementation
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.validator.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor() : ViewModel() {


    val loadEvent = MutableLiveData<Results>()

    @Inject
    lateinit var storage: SharedPreferencesStorageImplementation

    @Inject
    lateinit var contactsDatabase: ContactsDatabase

    @Inject
    lateinit var validator: Validator

    fun initializeData() {
        loadEvent.value = Results.OK
    }

    fun saveLoginData(login: String, password: String) {
        storage.save("${Constants.PROFILE_LOGIN} $login", login)
        storage.save("${Constants.PROFILE_PASSWORD} $password", password)
        storage.save(Constants.LAST_SAVED_LOGIN, login)
        storage.save(Constants.LAST_SAVED_PASSWORD, password)
    }

    fun isExistingAccount(login: String): Boolean {
        if (!storage.getString("${Constants.PROFILE_LOGIN} $login").isNullOrEmpty()) {
            loadEvent.value = Results.EXISTED_ACCOUNT_ERROR
            return true
        }
        return false
    }
}