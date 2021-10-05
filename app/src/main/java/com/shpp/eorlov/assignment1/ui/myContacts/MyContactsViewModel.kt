package com.shpp.eorlov.assignment1.ui.myContacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.UserRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyContactsViewModel @Inject constructor(
    private val storage: SharedPreferencesStorageImpl,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    val userListLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val loadEvent = MutableLiveData<Results>()
    val selectedEvent = MutableLiveData(false)

    init {
        viewModelScope.launch {
            if (userListLiveData.value == null) {
                loadEvent.value = Results.INITIALIZE_DATA_ERROR
            } else {
                val users = userRepository.getAll().toMutableList()
                if (users.isNotEmpty()) {
                    userListLiveData.value = users
                } else {
                    loadEvent.value = Results.INITIALIZE_DATA_ERROR
                }
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

    fun addItems(addedItems: MutableList<UserModel>) {
        viewModelScope.launch {
//            userRepository.clearTable() //todo remove
            userRepository.insertAll(*addedItems.toTypedArray())
            loadEvent.value = Results.LOADING
            userListLiveData.value = userRepository.getAll().toMutableList()
        }
    }
}