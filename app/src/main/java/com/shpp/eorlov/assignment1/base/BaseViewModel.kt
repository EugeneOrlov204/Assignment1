package com.shpp.eorlov.assignment1.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.utils.Results

abstract class BaseViewModel : ViewModel() {
    val loadEventLiveData = MutableLiveData<Results>()

    fun ok() {
        loadEventLiveData.value = Results.OK
    }

    fun loading() {
        loadEventLiveData.value = Results.LOADING
    }

    fun internetError() {
        loadEventLiveData.value = Results.INTERNET_ERROR
    }

    fun unexpectedResponse() {
        loadEventLiveData.value = Results.UNEXPECTED_RESPONSE
    }

    fun notSuccessfulResponse() {
        loadEventLiveData.value = Results.NOT_SUCCESSFUL_RESPONSE
    }
}
