package com.example.instabug

import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class listholder(view: View, var activity: FragmentActivity) : RecyclerView.ViewHolder(view) {
    lateinit var name: TextView
    lateinit var count: TextView
    init {
        name = view.findViewById<View>(R.id.name) as TextView
        count = view.findViewById<View>(R.id.count) as TextView



    }
}