package com.shpp.eorlov.assignment1.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.base.BaseViewModel
import com.shpp.eorlov.assignment1.model.*
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor() : BaseViewModel() {
    val newUserLiveData = MutableLiveData<UserModel?>(null)
    val updatedUserLiveData = MutableLiveData<UserModel?>(null)
    val addUserLiveData = MutableLiveData(false)
}