package com.shpp.eorlov.assignment1.ui.myContacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImplementation
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.UserRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyContactsViewModel @Inject constructor(
    private val storage: SharedPreferencesStorageImplementation,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    val userListLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val loadEvent = MutableLiveData<Results>()
    val selectedEvent = MutableLiveData(false)

    fun initializeData(users: ArrayList<UserModel>) {
        if (userListLiveData.value == null) {
            loadEvent.value = Results.INITIALIZE_DATA_ERROR
        } else {
            loadEvent.value = Results.LOADING
            if (users.isNotEmpty()) {
                loadEvent.value = Results.OK
                userListLiveData.value = users
            } else {
                loadEvent.value = Results.INITIALIZE_DATA_ERROR
            }
        }
    }

    /**
     * Returns item from dataset
     */
    fun getItem(position: Int): UserModel? {
        return userListLiveData.value?.get(position)
    }

    /**
     *  Removes item by clicking to button
     */
    fun removeItem(position: Int) {
        viewModelScope.launch {
            userListLiveData.value?.get(position)?.let { userRepository.delete(it) }
        }
        userListLiveData.value?.removeAt(position)
        userListLiveData.value = userListLiveData.value
    }

    fun removeItem(item: UserModel?) {
        viewModelScope.launch {
            item?.let { userRepository.delete(it) }
        }
        userListLiveData.value?.remove(item)
        userListLiveData.value = userListLiveData.value
    }

    /**
     * Adds item to dataset by given position
     */
    fun addItem(position: Int, addedItem: UserModel) {
        viewModelScope.launch {
            userRepository.insertAll(addedItem)
        }
        userListLiveData.value?.add(position, addedItem)
        userListLiveData.value = userListLiveData.value
    }

    /**
     * Adds item to dataset in the end of list
     */
    fun addItem(addedItem: UserModel) {
        viewModelScope.launch {
            userRepository.insertAll(addedItem)
        }
        userListLiveData.value?.add(addedItem)
        userListLiveData.value = userListLiveData.value
    }

    fun fetchToken(): String {
        return storage.getString(Constants.ACCESS_TOKEN) ?: ""
    }

    fun clearContactsList() {
        userListLiveData.value?.clear()
        userListLiveData.value = userListLiveData.value
    }
}