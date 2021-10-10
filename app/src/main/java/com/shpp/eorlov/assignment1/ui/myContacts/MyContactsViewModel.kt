package com.shpp.eorlov.assignment1.ui.myContacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.base.BaseViewModel
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.UserRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyContactsViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl
) : BaseViewModel() {

    val contactsLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())

    init {
        if (contactsLiveData.value == null) {
            loadEventLiveData.value = Results.INITIALIZE_DATA_ERROR
        } else {
            refreshContactsList()
        }
    }

    fun refreshContactsList() {
        viewModelScope.launch {
            val users = userRepository.getAll().toMutableList()
            if (users.isNotEmpty()) {
                contactsLiveData.value = users
            } else {
                loadEventLiveData.value = Results.INITIALIZE_DATA_ERROR
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
            contactsLiveData.value?.get(position)?.let {
                userRepository.delete(it.id)

                contactsLiveData.value?.removeAt(position)
                contactsLiveData.value = contactsLiveData.value
            }
        }
    }

    fun removeItem(item: UserModel?) {
        viewModelScope.launch {
            item?.let {
                userRepository.delete(it.id)
                contactsLiveData.value?.remove(item)
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
}