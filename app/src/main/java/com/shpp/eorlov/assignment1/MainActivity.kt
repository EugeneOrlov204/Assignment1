package com.shpp.eorlov.assignment1


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shpp.eorlov.assignment1.databinding.ActivityMainBinding
import com.shpp.eorlov.assignment1.utils.ext.loadImage


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.imageViewPersonImage.loadImage(R.mipmap.lucile_alvarado)


        setNameOfPerson()

        binding.buttonEditProfile.setOnClickListener {
            goToAuthActivity()
        }


        setContentView(binding.root)
    }

    private fun setNameOfPerson() {
        val messageText = binding.textViewPersonName
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