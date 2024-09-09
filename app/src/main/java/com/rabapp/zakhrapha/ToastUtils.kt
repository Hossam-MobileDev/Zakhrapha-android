package com.rabapp.zakhrapha

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast

object ToastUtils {



        fun showCustomToast(context: Context, message: String) {
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.custom_toast, null)

            val toastText = layout.findViewById<TextView>(R.id.toast_text)
            toastText.text = message

            val toast = Toast(context)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.setGravity(Gravity.TOP, 0, 100)
            toast.show()
        }

}