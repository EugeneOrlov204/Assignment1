package com.shpp.eorlov.assignment1.ui.signin


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.di.SharedPrefStorage
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.storage.Storage
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.validator.Validator
import javax.inject.Inject


class SignInViewModel @Inject constructor() : ViewModel() {


    val loadEvent = MutableLiveData<Results>()

    @Inject
    @field:SharedPrefStorage
    lateinit var storage: Storage

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

    fun isNotExistingAccount(login: String): Boolean {
        if (!storage.getString("${Constants.PROFILE_LOGIN} $login").isNullOrEmpty()) {
            return false
        }
        loadEvent.value = Results.NOT_EXISTED_ACCOUNT_ERROR
        return true
    }

    fun isWrongPassword(password: String): Boolean {
        if (!storage.getString("${Constants.PROFILE_PASSWORD} $password").isNullOrEmpty()) {
            return false
        }
        loadEvent.value = Results.INVALID_PASSWORD
        return true
    }

    fun getLogin(): String? {
        return storage.getString(Constants.LAST_SAVED_LOGIN)
    }

    fun getPassword(): String? {
        return storage.getString(Constants.LAST_SAVED_PASSWORD)
    }
}