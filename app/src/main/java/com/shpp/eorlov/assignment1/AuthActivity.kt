package com.shpp.eorlov.assignment1

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle


class AuthActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_auth)

        val intent = intent

        if (intent.getStringExtra("username") != null) {
            val usernameField = findViewById<View>(R.id.edittext_auth_email) as EditText
            val passwordField = findViewById<View>(R.id.edittext_auth_password) as EditText
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
        val usernameField = findViewById<View>(R.id.edittext_auth_email) as EditText
        val passwordField = findViewById<View>(R.id.edittext_auth_password) as EditText
        var username = usernameField.text.toString()
        var password = passwordField.text.toString()

        //Finish the activity if it's is on resume state
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            finish()
        }

        val rememberMe = findViewById<View>(R.id.checkbox_auth_rememberme) as CheckBox
        if (!rememberMe.isChecked) {
            username = ""
            password = ""
        }

        startActivity(getDataToSend(username, password))
    }


    /**
     * Returns data to be send to another activity
     */
    private fun getDataToSend(username: String?, password: String?): Intent? {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("password", password)
        println("username = " + username)
        println("password = " + password)
        println("username = " + intent.getStringExtra("username"))
        println("password = " + intent.getStringExtra("password"))
        return intent
    }

}