package com.shpp.eorlov.assignment1.utils

object Constants {
    const val MIN_SIZE_OF_USERNAME = 3
    const val MIN_SIZE_OF_PHONE_NUMBER = 10

    //General
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val BUTTON_CLICK_DELAY: Long = 2000
    const val INVALID_CREDENTIALS_CODE = 403
    const val SUCCESS_RESPONSE_CODE = 200

    //Validator
    const val MIN_SIZE_OF_PASSWORD: Int = 8
    const val DATE_REGEX_PATTERN = "^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"

    //ContactsDatabase
    const val DEFAULT_PATH_TO_IMAGE = "https://i.pravatar.cc/50"

    //MyContactsFragment
    const val SNACKBAR_DURATION: Int = 5000
    const val LIST_OF_CONTACTS_KEY = "List of contacts"
    const val CONTACT_DIALOG_TAG = "Contact dialog"

    //SharedPreferencesStorage
    const val PREFS_FILE = "Storage"
    const val USER_TOKEN = "user_token"

    //ContactRecyclerAdapter
    const val CONTACT_VIEW_HOLDER_TYPE_CODE = 0

    //ContactCollectionAdapter
    const val AMOUNT_OF_VIEWPAGER_ITEMS = 2
    const val REGISTERED_USER_MODEL_KEY = "Registered user"

    //Sign up extended
    const val IMAGE_LOADER_DIALOG_TAG = "Image loader dialog"
}