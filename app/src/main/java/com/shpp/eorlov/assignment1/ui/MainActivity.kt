package com.shpp.eorlov.assignment1.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.App
import com.shpp.eorlov.assignment1.di.ContactComponent

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    // Stores an instance of RegistrationComponent so that its Fragments can access it
    lateinit var contactComponent: ContactComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        // Creates an instance of Registration component by grabbing the factory from the app graph
        contactComponent = (application as App).appComponent.contactComponent().create()

        // Injects this activity to the just created Registration component
        contactComponent.inject(this)

        super.onCreate(savedInstanceState)
    }
}


