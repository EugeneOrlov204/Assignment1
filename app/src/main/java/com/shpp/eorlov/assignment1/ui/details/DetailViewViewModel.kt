package com.shpp.eorlov.assignment1.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.db.ContactsDatabaseImpl
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewViewModel @Inject constructor(
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
}