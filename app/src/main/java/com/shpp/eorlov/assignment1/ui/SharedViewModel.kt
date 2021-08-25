package com.shpp.eorlov.assignment1.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.model.UserModel
import javax.inject.Inject

class SharedViewModel @Inject constructor() : ViewModel() {
    val newUser = MutableLiveData<UserModel>()
}