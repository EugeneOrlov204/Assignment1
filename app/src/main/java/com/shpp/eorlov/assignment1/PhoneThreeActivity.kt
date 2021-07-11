package com.shpp.eorlov.assignment1


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class PhoneThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_phone3)
        Glide.with(this)
            .load(R.mipmap.avatar2)
            .circleCrop()
            .into(findViewById(R.id.personImageImageView))

        //ViewBinding
        //ЧТо есть у Glide
    }

    fun goToPhoneOne(view: View) {
        val intent = Intent(this, PhoneOneActivity::class.java)
        startActivity(intent)
    }
}