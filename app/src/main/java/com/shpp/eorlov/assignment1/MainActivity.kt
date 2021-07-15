package com.shpp.eorlov.assignment1


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        setContentView(R.layout.activity_main)
        Glide.with(this)
            .load(R.mipmap.lucile_alvarado)
            .circleCrop()
            .into(findViewById(R.id.image_view_person_image))

        val intent = intent
        username = intent.getStringExtra("username")
        password = intent.getStringExtra("password")

        if (username != "" && username != null) {
            val messageText = findViewById<View>(R.id.text_view_person_name) as TextView
            messageText.text = username
        }
    }

    /**
     * Change current activity to AuthActivity
     */
    fun goToAuthActivity(view: View) {
        startActivity(getDataToSend(username, password))
        finish()
    }


    /**
     * Returns data to be send to another activity
     */
    private fun getDataToSend(username: String?, password: String?): Intent {
        val intent = Intent(this, AuthActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("password", password)
        return intent
    }
}