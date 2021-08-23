package com.shpp.eorlov.assignment1.ui.mainfragment.adapter

import android.os.SystemClock
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.ext.clicks
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs

class ContactsViewHolder(
    private val binding: ListItemBinding,
    private val onContactClickListener: ContactClickListener,
    private val lifecycleScope: LifecycleCoroutineScope
) :
    RecyclerView.ViewHolder(binding.root) {


    private lateinit var userModel: UserModel
    fun bindTo(userModel: UserModel){
        this.userModel = userModel

        binding.apply {
            userModel.apply {
                textViewPersonName.text = name
                textViewPersonProfession.text = profession
                draweeViewPersonImage.setImageURI(photo)
                setListeners()
            }
        }
    }



    private var previousClickTimestamp = SystemClock.uptimeMillis()

    private fun setListeners() {
        binding.imageViewRemoveButton.clicks()
            .onEach {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    onContactClickListener.onContactRemove(bindingAdapterPosition)
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }
            .launchIn(lifecycleScope)


        binding.constraintLayoutContact.clicks()
            .onEach {
                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    onContactClickListener.onContactSelected(userModel)
                    previousClickTimestamp = SystemClock.uptimeMillis()

                }
            }
            .launchIn(lifecycleScope)

    }
}