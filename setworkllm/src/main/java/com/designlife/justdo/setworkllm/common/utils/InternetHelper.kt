package com.designlife.justdo.setworkllm.common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

internal class InternetHelper(
    private val context: Context
) {
    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }


    fun observeInternet(
        onAvailable: (state : Boolean) -> Unit
    ): ConnectivityManager.NetworkCallback {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                onAvailable(true)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                onAvailable(false)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                onAvailable(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val hasInternet =
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

                onAvailable(hasInternet)
            }
        }

        cm.registerDefaultNetworkCallback(callback)

        return callback
    }
}