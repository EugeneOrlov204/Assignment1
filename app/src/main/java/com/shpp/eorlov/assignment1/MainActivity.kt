package com.shpp.eorlov.assignment1


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {

    var username: String? = null
        set(value) {
            field = value
        }
        get() {
            return field
        }
    private var password: String? = null
        set(value) {
            field = value
        }
        get() {
            return field
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        Glide.with(this)
            .load(R.mipmap.eugene_orlov)
            .circleCrop()
            .into(findViewById(R.id.imageview_main_imageofperson))

        val intent = intent
        username = intent.getStringExtra("username")
        password = intent.getStringExtra("password")
        println("username = " + username)
        println("password = " + password)
        val messageText = findViewById<View>(R.id.textview_main_nameofperson) as TextView
        messageText.text = username
    }

    /**
     * Change current activity to AuthActivity
     */
    fun goToAuthActivity(view: View) {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)

        //Finish the activity if it's is on resume state
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            finish()
        }

        startActivity(getDataToSend(username, password))
    }


    /**
     * Returns data to be send to another activity
     */
    private fun getDataToSend(username: String?, password: String?): Intent? {
        val intent = Intent(this, AuthActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("password", password)
        return intent
    }
}