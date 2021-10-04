package com.shpp.eorlov.assignment1.ui.myprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.Constants.ACCESS_TOKEN
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val storage: SharedPreferencesStorageImpl
) : ViewModel() {

    val userLiveData: MutableLiveData<UserModel> by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData(
            UserModel(
                name = "",
                career = "",
                photo = "",
                address = "",
                birthday = "",
                phone = "",
                email = ""
            )
        )
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
        return userModel.birthday?.isNotEmpty() ?: false &&
                userModel.email?.isNotEmpty() ?: false &&
                userModel.name?.isNotEmpty() ?: false &&
                userModel.phone?.isNotEmpty() ?: false &&
                userModel.photo?.isNotEmpty() ?: false &&
                userModel.career?.isNotEmpty() ?: false &&
                userModel.address?.isNotEmpty() ?: false

    }

    fun updateProfile(userModel: UserModel) {
        userLiveData.value = userModel
    }

    fun fetchToken(): String {
        return storage.getString(ACCESS_TOKEN) ?: ""
    }
}