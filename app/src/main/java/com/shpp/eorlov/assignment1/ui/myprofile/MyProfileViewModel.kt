package com.shpp.eorlov.assignment1.ui.myprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.Results
import javax.inject.Inject

class MyProfileViewModel @Inject constructor() : ViewModel() {

    val userLiveData: MutableLiveData<UserModel> by lazy {
        MutableLiveData(contactsDatabase.getDefaultUserModel())
    }

    val loadEvent = MutableLiveData<Results>()


    @Inject
    lateinit var contactsDatabase: ContactsDatabase

    fun initializeData() {
        if (userLiveData.value == null) {
            loadEvent.value = Results.INITIALIZE_DATA_ERROR
        } else {
            loadEvent.value = Results.LOADING

            val data = contactsDatabase.getUserModelFromStorage()
            userLiveData.value = data

            loadEvent.value = Results.OK
        }
    }

    fun saveData() {

        contactsDatabase.saveUserModelToStorage(userLiveData.value)

    }


    /**
     * Returns true if all field of profile in
     * fragment_edit_profile is filled out,
     * otherwise false
     */
    fun isProfileFilledOut(): Boolean {
        val userModel = userLiveData.value ?: return false
        return userModel.birthDate.isNotEmpty() &&
                userModel.email.isNotEmpty() &&
                userModel.name.isNotEmpty() &&
                userModel.phoneNumber.isNotEmpty() &&
                userModel.photo.isNotEmpty() &&
                userModel.profession.isNotEmpty() &&
                userModel.residenceAddress.isNotEmpty()

    }

    fun updateProfile(list: UserModel) {
        userLiveData.value = list
    }
}