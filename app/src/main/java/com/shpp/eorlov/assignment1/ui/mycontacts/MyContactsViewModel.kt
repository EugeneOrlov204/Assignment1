package com.shpp.eorlov.assignment1.ui.mycontacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.Results
import javax.inject.Inject


class MyContactsViewModel @Inject constructor() : ViewModel() {

    val userListLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val loadEvent = MutableLiveData<Results>()
    val selectedEvent = MutableLiveData(false)

    @Inject
    lateinit var contactsDatabase: ContactsDatabase

    fun initializeData() {
        if (userListLiveData.value == null) {
            loadEvent.value = Results.INITIALIZE_DATA_ERROR
        } else {
            loadEvent.value = Results.LOADING
            val data = contactsDatabase.listOfContacts
            if (data.isNotEmpty()) {
                loadEvent.value = Results.OK
                userListLiveData.value = data
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
        userListLiveData.value?.removeAt(position)
        userListLiveData.value = userListLiveData.value
    }

    /**
     * Adds item to dataset by given position
     */
    fun addItem(position: Int, addedItem: UserModel) {
        userListLiveData.value?.add(position, addedItem)
        userListLiveData.value = userListLiveData.value
    }

    /**
     * Adds item to dataset in the end of list
     */
    fun addItem(addedItem: UserModel) {
        userListLiveData.value?.add(addedItem)
        userListLiveData.value = userListLiveData.value
    }

    fun selectAllContacts() {
        userListLiveData.apply {
            val list = value ?: emptyList()
            for(item in list) {
                item.selected = true
            }
        }

        userListLiveData.value = userListLiveData.value
    }
}