package com.shpp.eorlov.assignment1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class AuthActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val intent = intent

        if (intent.getStringExtra("username") != null) {
            val usernameField = findViewById<EditText>(R.id.edit_text_email)
            val passwordField = findViewById<EditText>(R.id.edit_text_password)
            val username = intent.getStringExtra("username")
            val password = intent.getStringExtra("password")
            usernameField.setText(username)
            passwordField.setText(password)
        }
    }


    /**
     * Change current activity to MainActivity
     */
    fun goToMainActivity(view: View) {
        val usernameField = findViewById<EditText>(R.id.edit_text_email)
        val passwordField = findViewById<EditText>(R.id.edit_text_password)
        var username = usernameField.text.toString()
        var password = passwordField.text.toString()


        val rememberMe = findViewById<View>(R.id.check_box_remember_me) as CheckBox
        if (!rememberMe.isChecked) {
            username = ""
            password = ""
        }

        startActivity(getDataToSend(username, password))
        finish()
    }

    /**
     * Returns data to be send to another activity
     */
    private fun getDataToSend(username: String?, password: String?): Intent {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("password", password)
        return intent
    }
}