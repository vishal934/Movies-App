package com.example.movies.utils

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.isPortrait() =resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

fun Context.showToast(text:String?){
    Toast.makeText(this,text, Toast.LENGTH_LONG).show()
}
fun Context.isNetworkAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = cm.activeNetwork ?: return false
        val actNw = cm.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION")
        val nwInfo = cm.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return nwInfo.isConnected
    }
}
