package com.shpp.eorlov.assignment1.db

import android.content.Context
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.Constants.DEFAULT_PATH_TO_IMAGE
import javax.inject.Inject


class ContactsDatabase @Inject constructor(private val context: Context) : LocalDB {

    val listOfContacts: MutableList<UserModel> by lazy { loadPersonData() }
    
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
                    i,
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

    /**
     * Returns list of careers
     * Temporary hardcoded
     */
    override fun getCareers(): List<String> {
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
    override fun getNames(): List<String> {
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
    override fun getEmails(): List<String> {
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
    override fun getResidence(): List<String> {
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