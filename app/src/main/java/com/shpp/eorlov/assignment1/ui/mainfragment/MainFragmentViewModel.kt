package com.shpp.eorlov.assignment1.ui.mainfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.Results
import javax.inject.Inject


class MainFragmentViewModel @Inject constructor() : ViewModel() {

    val userListLiveData = MutableLiveData<MutableList<UserModel>>(ArrayList())
    val loadEvent = MutableLiveData<Results>()

    @Inject
    lateinit var contactsDatabase: ContactsDatabase

    /**
     * Returns value of dataset
     */
    fun getPersonData() {
        if (userListLiveData.value == null) {
            loadEvent.value = Results.INIT_RECYCLER_VIEW_ERROR
        } else {
            loadEvent.value = Results.LOADING
            val data = contactsDatabase.listOfContacts
            if (data.isNotEmpty()) {
                loadEvent.value = Results.OK
                userListLiveData.value = data
            } else {
                loadEvent.value = Results.INIT_RECYCLER_VIEW_ERROR
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
}