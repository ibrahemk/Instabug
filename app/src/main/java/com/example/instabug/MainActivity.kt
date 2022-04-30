package com.example.instabug

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.reflect.Type
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    var recycllist: RecyclerView? = null
    var sort: ImageView? = null
    var searchicon: ImageView? = null
    var search: EditText? = null
    var title: TextView? = null
    var empty: TextView? = null
    lateinit var list:ArrayList<Word>
    lateinit var model:List_model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()


        recycllist=findViewById<RecyclerView>(R.id.list)
        sort=findViewById<ImageView>(R.id.sorticon)
        searchicon=findViewById<ImageView>(R.id.searchicon)
        search=findViewById<EditText>(R.id.search)
        title=findViewById<TextView>(R.id.title)
        empty=findViewById<TextView>(R.id.empty)

 model=List_model(activity = this)

        model.handleui()
    }






}







