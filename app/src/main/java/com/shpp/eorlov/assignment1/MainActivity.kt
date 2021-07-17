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
        restoreUIElementsLogic()
        setNameOfPerson(name = intent.getStringExtra("personName").toString())

        binding.buttonEditProfile.setOnClickListener {
            goToAuthActivity()
        }

        setContentView(binding.root)
    }

    /**
     * Restores UI elements states.
     * For example button the button has become enable
     */
    private fun restoreUIElementsLogic() {
        binding.buttonEditProfile.isEnabled = true
    }

    /**
     * Set parsed intent's string to title of person's image
     */
    private fun setNameOfPerson(name : String) {
        val messageText = binding.textViewPersonName
        messageText.text = name
    }

    /**
     * Change current activity to AuthActivity
     */
    private fun goToAuthActivity() {
        binding.buttonEditProfile.isEnabled = false
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}


