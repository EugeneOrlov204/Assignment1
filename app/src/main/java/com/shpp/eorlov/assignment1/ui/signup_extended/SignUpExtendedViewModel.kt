package com.shpp.eorlov.assignment1.ui.signup_extended

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.utils.Constants.CURRENT_EMAIL
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(private val storage: SharedPreferencesStorageImpl) :
    ViewModel() {

    val loadEvent = MutableLiveData<Results>()

    fun initializeData() {
        loadEvent.value = Results.OK
    }

    fun rememberCurrentEmail(email: String) {
        storage.save(CURRENT_EMAIL, email)
    }
}