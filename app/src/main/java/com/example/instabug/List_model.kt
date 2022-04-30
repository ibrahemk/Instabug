package com.example.instabug

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*
import java.util.concurrent.Executors

class List_model(var activity: MainActivity): ViewModel() {
    var progress: ProgressDialog? = null
fun handleui(){
    showloading(true)
    savesort("d",activity)
handlesearch()
    activity.sort!!.setOnClickListener(sortaction())
    var mLayoutManager = LinearLayoutManager(activity)
   activity.recycllist!!.layoutManager = mLayoutManager
    activity.recycllist!!.itemAnimator = DefaultItemAnimator()

    activity.list=ArrayList<Word>()


    // v.setEnabled(false);
    val check = CheckInternetConnection(activity).haveNetworkConnection()
    if (check){
    calthread { result ->
            activity.runOnUiThread(Runnable {
                if (result.isNullOrEmpty()||result.contains("errorkhalil")){
                  showempty(true)
                    showloading(false)
                }else{
                activity.list=listparse(result)
                saveData(result,activity)
                if (activity.list.size>0){
                var adapter:Listadapter= Listadapter(activity,activity.list)
                activity.recycllist!!.adapter=adapter
showloading(false)
//                activity.list=loadData(activity)
                    showempty(false)
                }else{
                    showempty(true)
                }


                }

                MyIdlingResource.decrement();
            })


    }}else{
        showloading(true)
        activity.list=loadData(activity)
        if (activity.list.size>0) {
            var adapter: Listadapter = Listadapter(activity, activity.list)
            activity.recycllist!!.adapter = adapter
            showempty(false)
        }else{
            showempty(true)
        }
        showloading(false)
    }

}

    fun listparse(result:String):ArrayList<Word>{
        var occurrences: MutableMap<String, Word> = HashMap()
        var list=ArrayList<Word>()
        for (word in result.split(" ")) {
            var w:Word

            if (occurrences.containsKey(word)) {
                w= occurrences[word]!!

            }else{
                w= Word()
                w.wordkey=word
            }
            w.wordcount=w.wordcount+1
            occurrences[word] = w
        }
        for ((key, value) in occurrences) {
            list.add(value)
        }



       if (loadsort(activity).isNotEmpty()&&loadsort(activity)=="a"){
    list.sortBy { it.wordcount }
           activity.sort!!.setImageResource(R.drawable.ic_asce_sort_24)

       }  else if (loadsort(activity).isNotEmpty()&&loadsort(activity)=="d"){
    list.sortByDescending { it.wordcount }
           activity.sort!!.setImageResource(R.drawable.ic_desce_sort_24)

       }


        return list
    }

    fun calthread(
        callback: (String) -> Unit
    ) {
        var executor = Executors.newFixedThreadPool(5)

        executor.execute {
            MyIdlingResource.increment();
            try {

                val response = firethread()
                callback(response)
            } catch (e: Exception) {
                val errorResult = Result.Error(e)
                callback("$errorResult errorkhalil")
            }
        }
    }
    private fun firethread(): String {
        val page = "https://instabug.com/"
        //Connecting to the web page
        //Connecting to the web page
        val conn: Connection = Jsoup.connect(page)
        //executing the get request
        //executing the get request
        val doc: Document = conn.get()
        //Retrieving the contents (body) of the web page
        //Retrieving the contents (body) of the web page
        val result: String = doc.body().text()
        return result
        // HttpURLConnection logic
    }
    private fun loadData(activity: FragmentActivity):ArrayList<Word> {
        val sharedPreferences: SharedPreferences =activity.getSharedPreferences("kotlinsharedpreference", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("words", "")
        var ModalArrayList:ArrayList<Word> =listparse(json.toString())
        if (ModalArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            ModalArrayList = ArrayList()
        }
        return ModalArrayList
    }
    private fun saveData(list:String,activity: FragmentActivity) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        val sharedPreferences: SharedPreferences =activity.getSharedPreferences("kotlinsharedpreference", Context.MODE_PRIVATE)

        // creating a variable for editor to
        // store data in shared preferences.
        val editor = sharedPreferences.edit()


        // below line is to save data in shared
        // prefs in the form of string.
        editor.remove("words")
        editor.putString("words", list)

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply()

        // after saving data we are displaying a toast message.
//        Toast.makeText(activity, "Saved Array List . ", Toast.LENGTH_SHORT).show()
    }

     fun loadsort(activity: FragmentActivity):String{
        val sharedPreferences: SharedPreferences =activity.getSharedPreferences("kotlinsharedpreference", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("sort", "")
        return json.toString()
    }


    private fun savesort( type: String, activity: FragmentActivity) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.

        val sharedPreferences: SharedPreferences =activity.getSharedPreferences("kotlinsharedpreference", Context.MODE_PRIVATE)

        // creating a variable for editor to
        // store data in shared preferences.
        val editor = sharedPreferences.edit()


        // below line is to save data in shared
        // prefs in the form of string.
        if (!sharedPreferences.contains("sort")) {
            editor.putString("sort", "a")
        }else if (type.trim().isNotEmpty()){
            editor.remove("sort")
            editor.putString("sort", type)
        }

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply()

        // after saving data we are displaying a toast message.
//        Toast.makeText(activity, "Saved Array List . ", Toast.LENGTH_SHORT).show()
    }
fun sortaction():View.OnClickListener{
    return View.OnClickListener {
       sort()
    }
}
    fun handlesearch(){
        activity.searchicon!!.setOnClickListener(iconsearch_action())
        activity.search!!.addTextChangedListener(searchTextChange())
    }
    fun iconsearch_action():View.OnClickListener{
        return View.OnClickListener {
            if (activity.search!!.visibility==View.GONE&&activity.title!!.visibility==View.VISIBLE){
                activity.search!!.visibility=View.VISIBLE
                activity.title!!.visibility=View.GONE
                activity.searchicon!!.setImageResource(R.drawable.ic_x)


            }else if (activity.search!!.visibility==View.VISIBLE&& activity.search!!.text.toString().trim().isNotEmpty()){
                showloading(true)
                (activity.recycllist!!.adapter as Listadapter ).list=searchlist(activity.search!!.text.toString().trim())

                if (loadsort(activity).isNotEmpty()&&loadsort(activity)=="a") {
                    (activity.recycllist!!.adapter as Listadapter).list.sortBy { it.wordcount }
                }
                else if (loadsort(activity).isNotEmpty()&&loadsort(activity)=="d"){
                        (activity.recycllist!!.adapter as Listadapter).list.sortByDescending { it.wordcount }

                    }

                (activity.recycllist!!.adapter as Listadapter ).notifyDataSetChanged()
                showloading(false)
            }else if (activity.search!!.visibility==View.VISIBLE&& activity.search!!.text.toString().trim().isEmpty()){
                showloading(true)
                activity.search!!.visibility=View.GONE
                activity.title!!.visibility=View.VISIBLE
                activity.searchicon!!.setImageResource(R.drawable.ic_search)
                (activity.recycllist!!.adapter as Listadapter ).list=activity.list

                if (loadsort(activity).isNotEmpty()&&loadsort(activity)=="a") {
                    (activity.recycllist!!.adapter as Listadapter).list.sortBy { it.wordcount }
                }
                else if (loadsort(activity).isNotEmpty()&&loadsort(activity)=="d"){
                    (activity.recycllist!!.adapter as Listadapter).list.sortByDescending { it.wordcount }

                }

                (activity.recycllist!!.adapter as Listadapter ).notifyDataSetChanged()
                showloading(false)
            }
        }
    }
    fun searchlist(text:String):ArrayList<Word>{

        var filter_list= ArrayList<Word>()
        var list=(activity.recycllist!!.adapter as Listadapter ).list
        for (word in list){
            if (word.wordkey.contains(text)|| word.wordkey == text){
                filter_list.add(word)
            }
        }

return filter_list
    }


    fun sort(){
        if (activity.recycllist!!.adapter != null&&(activity.recycllist!!.adapter as Listadapter ).list.size>0) {
            if (loadsort(activity).isNotEmpty() && loadsort(activity) == "a") {
                activity.sort!!.setImageResource(R.drawable.ic_desce_sort_24)

                (activity.recycllist!!.adapter as Listadapter ).list.sortByDescending { it.wordcount }
                (activity.recycllist!!.adapter as Listadapter ).notifyDataSetChanged()
                savesort("d",activity)
            } else if (loadsort(activity).isNotEmpty() && loadsort(activity) == "d") {

                activity.sort!!.setImageResource(R.drawable.ic_asce_sort_24)
                (activity.recycllist!!.adapter as Listadapter ).list.sortBy { it.wordcount }
                (activity.recycllist!!.adapter as Listadapter ).notifyDataSetChanged()
                savesort("a",activity)
            }
        }
    }

    private fun searchTextChange(): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

//                to restart the list  and clean result of search if the text in field is remove
                if (s.isEmpty()) {

                    activity.searchicon!!.setImageResource(R.drawable.ic_x)
                }
                if (s.isNotEmpty()) {
                    activity.searchicon!!.setImageResource(R.drawable.ic_search)
                }
            }

            override fun afterTextChanged(s: Editable) {
//                to fire search async when end write in search name field
            }
        }
    }

    fun showloading( show: Boolean) {
        try {
            if (progress != null) {
                progress!!.cancel()
            }
            if (progress == null || progress != null && progress!!.ownerActivity != null && progress!!.ownerActivity!!.isFinishing
                || progress != null && progress!!.ownerActivity != null && progress!!.ownerActivity !== activity
            ) {
               progress = ProgressDialog(activity)
          progress!!.setCancelable(false)
            progress!!.setMessage(activity.getString(R.string.wait_while_loading))
            }
            if (progress != null && !progress!!.isShowing && show) {
               progress!!.show()
            } else if (progress != null && !show) {
              progress!!.cancel()
            }
        } catch (e: Exception) {
        }
    }

fun showempty(show: Boolean){
    if (show){
        activity.empty!!.visibility=View.VISIBLE
        activity.recycllist!!.visibility=View.GONE
        activity.searchicon!!.visibility=View.INVISIBLE
        activity.sort!!.visibility=View.INVISIBLE
    }else{
        activity.empty!!.visibility=View.GONE
        activity.recycllist!!.visibility=View.VISIBLE
        activity.searchicon!!.visibility=View.VISIBLE
        activity.sort!!.visibility=View.VISIBLE
    }

}
}