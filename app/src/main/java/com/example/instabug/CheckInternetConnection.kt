package com.example.instabug

import android.content.Context
import android.net.ConnectivityManager

class CheckInternetConnection(private val applicationContext: Context?) {
    var conManager: ConnectivityManager? = null
    fun haveNetworkConnection(): Boolean {
        var wifiAvailable = false
        var mobileAvailable = false
        if (applicationContext != null) {
            conManager =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
        if (conManager != null) {
            val networkInfo = conManager!!.allNetworkInfo
            for (netInfo in networkInfo) {
                if (netInfo.typeName.equals(
                        "WIFI",
                        ignoreCase = true
                    )
                ) if (netInfo.isConnected) wifiAvailable = true
                if (netInfo.typeName.equals(
                        "MOBILE",
                        ignoreCase = true
                    )
                ) if (netInfo.isConnected) mobileAvailable = true
            }
        }

        //  Log.e("Check_wifi_mobile", wifiAvailable + "======" + mobileAvailable);
        return wifiAvailable || mobileAvailable
    }
}
