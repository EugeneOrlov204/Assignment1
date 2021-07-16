package com.shpp.eorlov.assignment1


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Glide.with(this)
            .load(R.mipmap.lucile_alvarado)
            .circleCrop()
            .into(findViewById(R.id.image_view_person_image))


    }

    /**
     * Change current activity to AuthActivity
     */
    fun goToAuthActivity(view: View) {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}