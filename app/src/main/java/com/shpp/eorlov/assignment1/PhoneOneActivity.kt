package com.shpp.eorlov.assignment1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class PhoneOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone1)
    }

    fun goToPhoneThree(view: View) {
        val intent = Intent(this, PhoneThreeActivity::class.java)
        startActivity(intent)
    }
}