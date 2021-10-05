package com.shpp.eorlov.assignment1.ui.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() :
    ViewModel() {

    val loadEvent = MutableLiveData<Results>()

    fun initializeData() {
        loadEvent.value = Results.OK
    }
}