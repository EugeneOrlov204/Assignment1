package com.shpp.eorlov.assignment1.ui.mycontacts.adapter

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
    private val onContactClickListener: ContactClickListener
//    private val lifecycleScope: LifecycleCoroutineScope
) :
    RecyclerView.ViewHolder(binding.root) {


    private lateinit var userModel: UserModel
    fun bindTo(userModel: UserModel){

        this.userModel = userModel
        binding.draweeViewPersonImage.setImageURI(userModel.photo)
        binding.apply {

                textViewPersonName.text = userModel.name
                textViewPersonProfession.text = userModel.profession
                draweeViewPersonImage.setImageURI(userModel.photo)
//                setListeners()

        }
    }



    private var previousClickTimestamp = SystemClock.uptimeMillis()

//    private fun setListeners() {
//        binding.imageViewRemoveButton.clicks()
//            .onEach {
//                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
//                    onContactClickListener.onContactRemove(bindingAdapterPosition)
//                    previousClickTimestamp = SystemClock.uptimeMillis()
//                }
//            }
//            .launchIn(lifecycleScope)
//
//
//        binding.constraintLayoutContact.clicks()
//            .onEach {
//                if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
//                    onContactClickListener.onContactSelected(userModel)
//                    previousClickTimestamp = SystemClock.uptimeMillis()
//
//                }
//            }
//            .launchIn(lifecycleScope)
//
//    }
}