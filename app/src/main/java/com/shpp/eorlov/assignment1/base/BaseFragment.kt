package com.shpp.eorlov.assignment1.base

import android.view.WindowManager
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    protected fun lockUI() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    protected fun unlockUI() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}