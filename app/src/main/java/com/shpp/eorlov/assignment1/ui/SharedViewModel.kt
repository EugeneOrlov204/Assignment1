package com.shpp.eorlov.assignment1.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.model.*
import com.shpp.eorlov.assignment1.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    val newUserLiveData = MutableLiveData<UserModel?>(null)
    val updatedUserLiveData = MutableLiveData<UserModel?>(null)
    val newPhotoLiveData = SingleLiveEvent<String>()
}