package com.shpp.eorlov.assignment1.ui.myProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shpp.eorlov.assignment1.data.storage.SharedPreferencesStorageImpl
import com.shpp.eorlov.assignment1.model.Data
import com.shpp.eorlov.assignment1.model.ResponseModel
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.repository.MainRepositoryImpl
import com.shpp.eorlov.assignment1.utils.Constants.ACCESS_TOKEN
import com.shpp.eorlov.assignment1.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val storage: SharedPreferencesStorageImpl,
    private val repository: MainRepositoryImpl
) : ViewModel() {

    val userLiveData: MutableLiveData<UserModel> by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData(
            UserModel(
                name = "",
                career = "",
                image = "",
                address = "",
                birthday = "",
                phone = "",
                email = ""
            )
        )
    }
    val loadEventLiveData = MutableLiveData<Results>()

    init {
        viewModelScope.launch {
            val accessToken = fetchToken()
            loadEventLiveData.value = Results.LOADING
            val response = try {
                repository.getUser(accessToken = "Bearer $accessToken")
            } catch (exception: IOException) {
                loadEventLiveData.value = Results.INTERNET_ERROR
                return@launch
            } catch (exception: HttpException) {
                loadEventLiveData.value = Results.UNEXPECTED_RESPONSE
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                userLiveData.postValue(response.body()!!.data.user)
            } else {
                loadEventLiveData.value = Results.NOT_SUCCESSFUL_RESPONSE
            }
        }
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
                userModel.image?.isNotEmpty() ?: false &&
                userModel.career?.isNotEmpty() ?: false &&
                userModel.address?.isNotEmpty() ?: false

    }

    fun updateProfile(userModel: UserModel) {
        userLiveData.value = userModel
    }

    private fun fetchToken(): String {
        return storage.getString(ACCESS_TOKEN) ?: ""
    }
}