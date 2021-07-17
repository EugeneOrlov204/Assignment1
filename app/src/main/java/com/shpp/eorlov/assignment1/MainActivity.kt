package com.shpp.eorlov.assignment1


import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import com.shpp.eorlov.assignment1.utils.ext.loadImage


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<AppCompatImageView>(R.id.image_view_person_image).
        loadImage(R.mipmap.lucile_alvarado) //View Binding

        setNameOfPerson()

        findViewById<AppCompatButton>(R.id.button_edit_profile).setOnClickListener {
            goToAuthActivity()
        }
    }

    private fun setNameOfPerson() {
        val messageText = findViewById<TextView>(R.id.text_view_person_name)
        val message = intent.getStringExtra("personName").toString()
        messageText.text = message
    }

    /**
     * Change current activity to AuthActivity
     */
    private fun goToAuthActivity() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}