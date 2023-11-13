package com.harissabil.anidex.di.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_BLUETOOTH
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI

class ConnectivityChecker {

    /**
     * Check the device's connectivity status.
     *
     * @param mContext the context of the calling activity or application.
     *
     * @return true if the device has an active Wi-Fi, cellular, Bluetooth or Ethernet connection,
     * false otherwise.
     */
    operator fun invoke(mContext: Context): Boolean {
        val connectivityManager: ConnectivityManager = mContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: Network = connectivityManager
            .activeNetwork ?: return false
        val networkCapabilities: NetworkCapabilities = connectivityManager
            .getNetworkCapabilities(network) ?: return false
        return when {
            networkCapabilities.hasTransport(TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(TRANSPORT_BLUETOOTH) -> true
            networkCapabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}