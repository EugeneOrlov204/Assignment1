package com.shpp.eorlov.assignment1.ui.myContacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.model.DataUsers
import com.shpp.eorlov.assignment1.model.ResponseModel
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
class MyContactsViewModel @Inject constructor(
    private val storage: SharedPreferencesStorageImpl,
    private val userRepository: UserRepositoryImpl,
    private val repository: MainRepositoryImpl
) : ViewModel() {

    //todo migrate to set
    val contactsLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val usersLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val loadEventLiveData = MutableLiveData<Results>()
    val selectedEventLiveData = MutableLiveData(false)
    val allUsersLiveData = MutableLiveData<ResponseModel<DataUsers>>()

    init {
        viewModelScope.launch {
            if (contactsLiveData.value == null) {
                loadEventLiveData.value = Results.INITIALIZE_DATA_ERROR
            } else {
                val users = userRepository.getAll().toMutableList()
                if (users.isNotEmpty()) {
                    contactsLiveData.value = users
                } else {
                    loadEventLiveData.value = Results.INITIALIZE_DATA_ERROR
                }
            }
        }
    }

    fun getAllUsers(accessToken: String) {
        viewModelScope.launch {
            loadEventLiveData.value = Results.LOADING
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
                allUsersLiveData.postValue(response.body()!!)
            } else {
                loadEventLiveData.value = Results.NOT_SUCCESSFUL_RESPONSE
            }
        }
    }


    /**
     * Returns item from dataset
     */
    fun getItem(position: Int): UserModel? {
        return contactsLiveData.value?.get(position)
    }

    /**
     *  Removes item by clicking to button
     */
    fun removeItem(position: Int) {
        viewModelScope.launch {
            contactsLiveData.value?.get(position)?.let { userRepository.delete(it) }
        }
        contactsLiveData.value?.removeAt(position)
        contactsLiveData.value = contactsLiveData.value
    }

    fun removeItem(item: UserModel?) {
        viewModelScope.launch {
            item?.let { userRepository.delete(it) }
        }
        contactsLiveData.value?.remove(item)
        contactsLiveData.value = contactsLiveData.value
    }

    /**
     * Adds item to dataset by given position
     */
    fun addItem(position: Int, addedItem: UserModel) {
        contactsLiveData.value?.let {
            if (!it.contains(addedItem)) {
                viewModelScope.launch {
                    userRepository.insertAll(addedItem)
                }
                contactsLiveData.value?.add(position, addedItem)
                contactsLiveData.value = contactsLiveData.value
            }
        }
    }

    /**
     * Adds item to dataset in the end of list
     */
    fun addItem(addedItem: UserModel) {
        contactsLiveData.value?.let {
            if (!it.contains(addedItem)) {
                viewModelScope.launch {
                    userRepository.insertAll(addedItem)
                }
                contactsLiveData.value?.add(addedItem)
                contactsLiveData.value = contactsLiveData.value
            }
        }
    }

    fun fetchToken(): String {
        return storage.getString(Constants.ACCESS_TOKEN) ?: ""
    }

    fun clearContactsList() {
        contactsLiveData.value = ArrayList()
    }

    fun addItems(addedItems: MutableList<UserModel>) {
        viewModelScope.launch {
            contactsLiveData.value?.let {
                for (item in addedItems) {
                    if (!it.contains(item)) {
                        userRepository.insertAll(item)
                        loadEventLiveData.value = Results.LOADING
                    }
                }
                contactsLiveData.value = userRepository.getAll().toMutableList()
            }
        }
    }
}