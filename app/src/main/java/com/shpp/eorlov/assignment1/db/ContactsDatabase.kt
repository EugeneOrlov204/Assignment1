package com.shpp.eorlov.assignment1.db

import android.content.Context
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.di.SharedPrefStorage
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.storage.Storage
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.DEFAULT_PATH_TO_IMAGE
import javax.inject.Inject


class ContactsDatabase @Inject constructor(private val context: Context) : LocalDB {

    @Inject
    @field:SharedPrefStorage
    lateinit var storage: Storage

    val listOfContacts: MutableList<UserModel> by lazy { loadPersonData() }
    override fun getDefaultUserModel(): UserModel =
        UserModel(
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )

    override fun getUserModelFromStorage(): UserModel =
        UserModel(
            name = storage.getString(Constants.MY_PROFILE_NAME_KEY) ?: "",
            profession = storage.getString(Constants.MY_PROFILE_PROFESSION_KEY) ?: "",
            photo = storage.getString(Constants.MY_PROFILE_PHOTO_KEY) ?: "",
            residenceAddress = storage.getString(Constants.MY_PROFILE_RESIDENCE_KEY) ?: "",
            birthDate = storage.getString(Constants.MY_PROFILE_BIRTHDATE_KEY) ?: "",
            phoneNumber = storage.getString(Constants.MY_PROFILE_PHONE_KEY) ?: "",
            email = storage.getString(Constants.MY_PROFILE_EMAIL_KEY) ?: ""
        )


    override fun loadPersonData(): MutableList<UserModel> {
        val listOfNames: List<String> = getNames()
        val listOfProfessions: List<String> = getCareers()
        val listOfEmails: List<String> = getEmails()
        val listOfResidence: List<String> = getResidence()
        val urlOfPhoto = DEFAULT_PATH_TO_IMAGE
        val result = mutableListOf<UserModel>()
        for (i in 0..9) {
            result.add(
                UserModel(
                    listOfNames[i],
                    listOfProfessions[i],
                    urlOfPhoto + i,
                    listOfResidence[i],
                    "",
                    "",
                    listOfEmails[i]
                )
            )
        }


        return result
    }

    override fun saveUserModelToStorage(userModel: UserModel?) {
        userModel?.apply {
            storage.save(Constants.MY_PROFILE_NAME_KEY, name)
            storage.save(Constants.MY_PROFILE_PROFESSION_KEY, profession)
            storage.save(Constants.MY_PROFILE_PHOTO_KEY, photo)
            storage.save(Constants.MY_PROFILE_RESIDENCE_KEY, residenceAddress)
            storage.save(Constants.MY_PROFILE_BIRTHDATE_KEY, birthDate)
            storage.save(Constants.MY_PROFILE_PHONE_KEY, phoneNumber)
            storage.save(Constants.MY_PROFILE_EMAIL_KEY, email)
        }
    }

    /**
     * Returns list of careers
     * Temporary hardcoded
     */
    private fun getCareers(): List<String> {
        return listOf(
            context.getString(R.string.user1_profession),
            context.getString(R.string.user2_profession),
            context.getString(R.string.user3_profession),
            context.getString(R.string.user4_profession),
            context.getString(R.string.user5_profession),
            context.getString(R.string.user6_profession),
            context.getString(R.string.user7_profession),
            context.getString(R.string.user8_profession),
            context.getString(R.string.user9_profession),
            context.getString(R.string.user10_profession)
        )
    }

    /**
     * Returns list of names
     * Temporary hardcoded
     */
    private fun getNames(): List<String> {
        return listOf(
            context.getString(R.string.user1_name),
            context.getString(R.string.user2_name),
            context.getString(R.string.user3_name),
            context.getString(R.string.user4_name),
            context.getString(R.string.user5_name),
            context.getString(R.string.user6_name),
            context.getString(R.string.user7_name),
            context.getString(R.string.user8_name),
            context.getString(R.string.user9_name),
            context.getString(R.string.user10_name)
        )
    }

    /**
     * Returns list of names
     * Temporary hardcoded
     */
    private fun getEmails(): List<String> {
        return listOf(
            context.getString(R.string.user1_email),
            context.getString(R.string.user2_email),
            context.getString(R.string.user3_email),
            context.getString(R.string.user4_email),
            context.getString(R.string.user5_email),
            context.getString(R.string.user6_email),
            context.getString(R.string.user7_email),
            context.getString(R.string.user8_email),
            context.getString(R.string.user9_email),
            context.getString(R.string.user10_email),
        )
    }

    /**
     * Returns list of residence
     * Temporary hardcoded
     */
    private fun getResidence(): List<String> {
        return listOf(
            context.getString(R.string.user1_residence),
            context.getString(R.string.user2_residence),
            context.getString(R.string.user3_residence),
            context.getString(R.string.user4_residence),
            context.getString(R.string.user5_residence),
            context.getString(R.string.user6_residence),
            context.getString(R.string.user7_residence),
            context.getString(R.string.user8_residence),
            context.getString(R.string.user9_residence),
            context.getString(R.string.user10_residence),
        )
    }
}