package com.shpp.eorlov.assignment1.ui.addContacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorage
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.repository.UserRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
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
) : ViewModel() {

    val usersLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val isLoadedListLiveData = MutableLiveData(false)
    val searchedContactsLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val loadEventLiveData = MutableLiveData<Results>()

    private var accessToken: String = ""
        get() = fetchToken(field)


    init {
        loading()

        viewModelScope.launch {

            val response = try {
                repository.getAllUsers(accessToken = "Bearer $accessToken")
            } catch (e: IOException) {
                loadEventLiveData.value = Results.INTERNET_ERROR
                return@launch
            } catch (e: HttpException) {
                loadEventLiveData.value = Results.UNEXPECTED_RESPONSE
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                usersLiveData.postValue(response.body()!!.data.users)
            } else {
                loadEventLiveData.value = Results.NOT_SUCCESSFUL_RESPONSE
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

    /**
     * Searches contacts by given pattern.
     * Returns true if contacts were found, otherwise false
     */
    fun searchContacts(pattern: String) : Boolean {
        loading()
        usersLiveData.value.apply {
            searchedContactsLiveData.value =
                this?.filter { it.name?.indexOf(pattern) != -1 && it.name?.isNotEmpty() == true }
                    ?.toMutableList()
        }
        return searchedContactsLiveData.value?.isNotEmpty() == true
    }

    fun clearSearchedContacts() {
        searchedContactsLiveData.value?.clear()
        usersLiveData.value = usersLiveData.value
    }

    fun ok() {
        loadEventLiveData.value = Results.OK
    }

    fun loading() {
        loadEventLiveData.value = Results.LOADING
    }
}