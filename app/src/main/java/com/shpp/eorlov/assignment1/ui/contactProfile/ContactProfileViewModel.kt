package com.shpp.eorlov.assignment1.ui.contactProfile

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorage
import com.shpp.eorlov.assignment1.ui.myContacts.MyContactsViewModel
import com.shpp.eorlov.assignment1.utils.Constants.IS_ADDED_CONTACT
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactProfileViewModel @Inject constructor(
    private val storage: SharedPreferencesStorage
) : ViewModel(){
    fun saveResult(result: Boolean) {
        storage.save(IS_ADDED_CONTACT, result)
    }
}