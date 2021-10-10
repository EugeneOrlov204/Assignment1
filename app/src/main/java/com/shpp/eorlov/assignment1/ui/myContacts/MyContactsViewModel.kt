package com.shpp.eorlov.assignment1.ui.myContacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.UserRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyContactsViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    val contactsLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val searchedContactsLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val loadEventLiveData = MutableLiveData<Results>()

    init {
        if (contactsLiveData.value == null) {
            loadEventLiveData.value = Results.INITIALIZE_DATA_ERROR
        } else {
            refreshContactsList()
        }
    }

    fun refreshContactsList() {
        loading()
        viewModelScope.launch {
            val users = userRepository.getAll().toMutableList()
            if (users.isNotEmpty()) {
                contactsLiveData.value = users
            }
        }
    }

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

    /**
     * Returns item from dataset
     */
    fun getItem(position: Int): UserModel? {
        return contactsLiveData.value?.get(position)
    }

    fun removeItem(item: UserModel?) {
        viewModelScope.launch {
            item?.let {
                userRepository.delete(it.id)
                contactsLiveData.value?.remove(item)
            }
            if (searchedContactsLiveData.value?.isNotEmpty() == true) {
                searchedContactsLiveData.value?.remove(item)
                searchedContactsLiveData.value = searchedContactsLiveData.value
            } else {
                contactsLiveData.value = contactsLiveData.value
            }
        }
    }

    /**
     * Adds item to dataset by given position
     */
    fun addItem(position: Int, addedItem: UserModel) {
        contactsLiveData.value?.let {
            if (!it.contains(addedItem)) {
                viewModelScope.launch {
                    userRepository.insertAll(addedItem)
                    contactsLiveData.value?.add(position, addedItem)
                    contactsLiveData.value = contactsLiveData.value
                }
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
                    contactsLiveData.value?.add(addedItem)
                    contactsLiveData.value = contactsLiveData.value
                }
            }
        }
    }

    /**
     * Searches contacts by given pattern.
     * Returns true if contacts were found, otherwise false
     */
    fun searchContacts(pattern: String) : Boolean {
        loading()
        contactsLiveData.value.apply {
            searchedContactsLiveData.value =
                this?.filter { it.name?.indexOf(pattern) != -1 && it.name?.isNotEmpty() == true }
                    ?.toMutableList()
        }
        return searchedContactsLiveData.value?.isNotEmpty() == true
    }

    fun clearSearchedContacts() {
        searchedContactsLiveData.value?.clear()
    }
}
