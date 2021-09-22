package com.shpp.eorlov.assignment1.ui.signup_extended

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabaseImplementation
import com.shpp.eorlov.assignment1.storage.SharedPreferencesStorage
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.validator.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor() : ViewModel() {

    val loadEvent = MutableLiveData<Results>()

    @Inject
    lateinit var storage: SharedPreferencesStorage

    @Inject
    lateinit var contactsDatabase: ContactsDatabaseImplementation

    @Inject
    lateinit var validator: Validator


    fun initializeData() {
        loadEvent.value = Results.OK
    }
}