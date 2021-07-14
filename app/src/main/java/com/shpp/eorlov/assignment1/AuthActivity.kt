package com.shpp.eorlov.assignment1

import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide


class AuthActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_test)

        val layout = findViewById<ConstraintLayout>(R.id.constraintlayout_auth_container)
        layout.startAnimation(animation)

        val intent = intent

        if (intent.getStringExtra("username") != null) {
            val usernameField = findViewById<EditText>(R.id.edittext_auth_email)
            val passwordField = findViewById<EditText>(R.id.edittext_auth_password)
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
        val usernameField = findViewById<EditText>(R.id.edittext_auth_email)
        val passwordField = findViewById<EditText>(R.id.edittext_auth_password)
        var username = usernameField.text.toString()
        var password = passwordField.text.toString()


        val rememberMe = findViewById<View>(R.id.checkbox_auth_rememberme) as CheckBox
        if (!rememberMe.isChecked) {
            username = ""
            password = ""
        }

        startActivity(getDataToSend(username, password))
        finish()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
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