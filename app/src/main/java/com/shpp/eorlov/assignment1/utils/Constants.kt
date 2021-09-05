package com.shpp.eorlov.assignment1.utils

object Constants {
    //General
    const val LAST_SAVED_LOGIN: String = "Last saved login"
    const val LAST_SAVED_PASSWORD: String = "Last saved password"
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val BUTTON_CLICK_DELAY: Long = 2000
    const val PROFILE_LOGIN = "Login"
    const val PROFILE_PASSWORD = "Password"

    //Validator
    const val MIN_SIZE_OF_PASSWORD: Int = 8
    const val DATE_REGEX_PATTERN = "^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"

    //ContactsDatabase
    const val MY_PROFILE_NAME_KEY: String = "My profile name"
    const val MY_PROFILE_PROFESSION_KEY: String = "My profile profession"
    const val MY_PROFILE_PHOTO_KEY: String = "My profile photo"
    const val MY_PROFILE_RESIDENCE_KEY: String = "My profile residence"
    const val MY_PROFILE_BIRTHDATE_KEY: String = "My profile birthdate"
    const val MY_PROFILE_PHONE_KEY: String = "My profile phone"
    const val MY_PROFILE_EMAIL_KEY: String = "My profile email"
    const val DEFAULT_PATH_TO_IMAGE = "https://i.pravatar.cc/50"

    //MyContactsFragment
    const val SNACKBAR_DURATION: Int = 5000
    const val LIST_OF_CONTACTS_KEY = "List of contacts"
    const val CONTACT_DIALOG_TAG = "Contact dialog"

    //SharedPreferencesStorage
    const val PREFS_FILE = "Storage"

    //ContactRecyclerAdapter
    const val CONTACT_VIEW_HOLDER_TYPE_CODE = 0

    //ContactCollectionAdapter
    const val AMOUNT_OF_VIEWPAGER_ITEMS = 2
    const val REGISTERED_USER_MODEL_KEY = "Registered user"
}