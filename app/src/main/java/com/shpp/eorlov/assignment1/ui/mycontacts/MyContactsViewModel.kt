package com.shpp.eorlov.assignment1.ui.mycontacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImplementation
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyContactsViewModel @Inject constructor(
    private val storage: SharedPreferencesStorageImplementation
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
        userListLiveData.value?.removeAt(position)
        userListLiveData.value = userListLiveData.value
    }

    fun removeItem(item: UserModel?) {
        userListLiveData.value?.remove(item)
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

    fun fetchToken(): String {
        return storage.getString(Constants.ACCESS_TOKEN) ?: ""
    }
}