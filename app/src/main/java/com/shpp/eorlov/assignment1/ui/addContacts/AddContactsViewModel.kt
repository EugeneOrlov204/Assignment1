package com.shpp.eorlov.assignment1.ui.addContacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.base.BaseViewModel
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorage
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.repository.UserRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddContactsViewModel @Inject constructor(
    private val storage: SharedPreferencesStorage,
    private val repository: MainRepositoryImpl,
    private val userRepository: UserRepositoryImpl
) : BaseViewModel() {

    val usersLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val isLoadedListLiveData = MutableLiveData(false)

    private var accessToken: String = ""
        get() = fetchToken(field)


    //todo how to show progress bar?
    init {
        loading()

        viewModelScope.launch {

            val response = try {
                repository.getAllUsers(accessToken = "Bearer $accessToken")
            } catch (e: IOException) {
                internetError()
                return@launch
            } catch (e: HttpException) {
                unexpectedResponse()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                usersLiveData.postValue(response.body()!!.data.users)
            } else {
                notSuccessfulResponse()
            }
        }
    }


    fun addItems(addedItems: MutableList<UserModel>) {
        viewModelScope.launch {
            userRepository.insertAll(*addedItems.toTypedArray())
            isLoadedListLiveData.value = true
        }
    }

    private fun fetchToken(token: String): String {
        return if (token.isEmpty()) {
            storage.getString(Constants.ACCESS_TOKEN) ?: ""
        } else token
    }
}