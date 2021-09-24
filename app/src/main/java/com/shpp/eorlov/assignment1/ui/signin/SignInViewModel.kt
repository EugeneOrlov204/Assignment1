package com.shpp.eorlov.assignment1.ui.signin


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabaseImpl
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorage
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.validator.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {


    val loadEvent = MutableLiveData<Results>()

    fun initializeData() {
        loadEvent.value = Results.OK
    }
}