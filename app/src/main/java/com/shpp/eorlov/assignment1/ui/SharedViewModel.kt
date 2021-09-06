package com.shpp.eorlov.assignment1.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.api.MainService
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(val repository: MainRepository) : ViewModel() {
    val newUser = MutableLiveData<UserModel?>(null)
    val updatedUser = MutableLiveData<UserModel?>(null)
    var job: Job? = null
    private val tag = SharedViewModel::class.java.name

    suspend fun registerUser(email: String, password: String): Pair<String, String> = withContext(Dispatchers.IO) {
        Log.d(tag, "Thread is ${Thread.currentThread().name}")
        val request = MainService.PostRequest(email, password)
        val response = repository.registerUser(request)
        if (response.isSuccessful) {
            val body = response.body()!!
            return@withContext Pair(body.json.email, body.json.password)
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}