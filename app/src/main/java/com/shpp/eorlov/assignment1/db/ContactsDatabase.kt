package com.shpp.eorlov.assignment1.db

import com.shpp.eorlov.assignment1.di.SharedPrefStorage
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.storage.Storage
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.DEFAULT_PATH_TO_IMAGE
import javax.inject.Inject


class ContactsDatabase @Inject constructor() : LocalDB {

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

    fun saveUserModelToStorage(userModel: UserModel) {
        TODO("Not yet implemented")
    }

    //todo move to JSON
    /**
     * Returns list of careers
     * Temporary hardcoded
     */
    private fun getCareers(): List<String> {
        return listOf(
            "Community worker",
            "Estate agent",
            "Pilot",
            "Dentist",
            "Clockmaker",
            "Barrister",
            "Auctioneer",
            "Printer",
            "Comedian",
            "Car dealer"
        )
    }

    /**
     * Returns list of names
     * Temporary hardcoded
     */
    private fun getNames(): List<String> {
        return listOf(
            "Darcy Benn",
            "Tatiana Matthewson",
            "Zandra Bailey",
            "Eliot Stevenson",
            "Mina Derrickson",
            "Gyles Breckinridge",
            "Sharlene Horsfall",
            "Milton Bryson",
            "Allissa Tindall",
            "Frannie Morriss"
        )
    }

    /**
     * Returns list of names
     * Temporary hardcoded
     */
    private fun getEmails(): List<String> {
        return listOf(
            "name1.surname@gmail.com",
            "name2.surname@gmail.com",
            "name3.surname@gmail.com",
            "name4.surname@gmail.com",
            "name5.surname@gmail.com",
            "name6.surname@gmail.com",
            "name7.surname@gmail.com",
            "name8.surname@gmail.com",
            "name9.surname@gmail.com",
            "name10.surname@gmail.com"
        )
    }

    /**
     * Returns list of residence
     * Temporary hardcoded
     */
    private fun getResidence(): List<String> {
        return listOf(
            "5295 Gaylord Walks Apk. 110",
            "5295 Gaylord Walks Apk. 111",
            "5295 Gaylord Walks Apk. 112",
            "5295 Gaylord Walks Apk. 113",
            "5295 Gaylord Walks Apk. 114",
            "5295 Gaylord Walks Apk. 115",
            "5295 Gaylord Walks Apk. 116",
            "5295 Gaylord Walks Apk. 117",
            "5295 Gaylord Walks Apk. 118",
            "5295 Gaylord Walks Apk. 119"
        )
    }
}