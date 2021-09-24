package com.shpp.eorlov.assignment1.ui.myprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabaseImpl
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val contactsDatabase: ContactsDatabaseImpl
) : ViewModel() {

    val userLiveData: MutableLiveData<UserModel> by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData(contactsDatabase.getDefaultUserModel())
    }

    val loadEvent = MutableLiveData<Results>()

    fun initializeData(userModel: UserModel) {
        if (userLiveData.value == null) {
            loadEvent.value = Results.INITIALIZE_DATA_ERROR
        } else {
            loadEvent.value = Results.LOADING

            userLiveData.value = userModel

            loadEvent.value = Results.OK
        }
    }

    //fixme remove hardcoded data
    fun saveData(login: String) {
//        contactsDatabase.saveUserModelToStorage(userLiveData.value ?: return, login)
    }


    /**
     * Returns true if all field of profile in
     * fragment_edit_profile is filled out,
     * otherwise false
     */
    fun isProfileFilledOut(): Boolean {
        val userModel = userLiveData.value ?: return false
        return userModel.birthDate?.isNotEmpty() ?: false &&
                userModel.email?.isNotEmpty() ?: false &&
                userModel.name?.isNotEmpty() ?: false &&
                userModel.phoneNumber?.isNotEmpty() ?: false &&
                userModel.photo?.isNotEmpty() ?: false &&
                userModel.profession?.isNotEmpty() ?: false &&
                userModel.residenceAddress?.isNotEmpty() ?: false

    }

    fun updateProfile(list: UserModel) {
        userLiveData.value = list
    }
}