package com.shpp.eorlov.assignment1.ui.signup


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.storage.SharedPreferencesStorageImplementation
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.validator.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() :
    ViewModel() {

    val loadEvent = MutableLiveData<Results>()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Results.REGISTER_ERROR.onError()
    }

    @Inject
    lateinit var storage: SharedPreferencesStorageImplementation

    @Inject
    lateinit var contactsDatabase: ContactsDatabase

    @Inject
    lateinit var validator: Validator

    var job: Job? = null


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun initializeData() {
        loadEvent.value = Results.OK
    }

    fun isExistingAccount(login: String): Boolean {
        if (!storage.getString("${Constants.PROFILE_LOGIN} $login").isNullOrEmpty()) {
            loadEvent.value = Results.EXISTED_ACCOUNT_ERROR
            return true
        }
        return false
    }


    private fun Results.onError() {
        loadEvent.value = this
    }
}