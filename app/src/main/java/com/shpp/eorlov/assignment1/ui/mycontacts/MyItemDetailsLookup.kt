package com.shpp.eorlov.assignment1.ui.mycontacts

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders.ContactsViewHolder

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as ContactsViewHolder)
                .getItemDetails()
        }
        return null
    }
}
