package com.danycarreto.firestore

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.ImageView
import com.bumptech.glide.Glide

@TargetApi(Build.VERSION_CODES.M)
    fun Context.isNetworkAvailable():Boolean{
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if(capabilities != null){
            when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->return true
            }
        }
        return false
    }

fun String.isCurpValidate():Boolean{
    return "^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}\$".toRegex().containsMatchIn(this)
}


fun ImageView.loadUrl(url:String){
    Glide.with(context).load(url).into(this)
}


val <T>List<T>.penultimoItem:Int
    get() = size -2
