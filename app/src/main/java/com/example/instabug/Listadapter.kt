package com.example.instabug

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class Listadapter(var activity: FragmentActivity, var list:ArrayList<Word>) : RecyclerView.Adapter<listholder> () {
    private var itemView: View? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listholder {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)
        return listholder(itemView!!,activity)
    }

    override fun onBindViewHolder(holder: listholder, position: Int) {
        var word: Word = list[position]
        if (!word.wordkey.isNullOrEmpty() ) {
            holder.name.text=word.wordkey
            holder.count.text=word.wordcount.toString()
        }
    }
    override fun getItemCount(): Int {
       return list.size
    }
}