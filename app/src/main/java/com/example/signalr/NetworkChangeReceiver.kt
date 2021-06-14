package com.example.signalr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val status = NetworkUtil.getConnectivityStatusString(context)
        Log.e("NetworkChangeReceiver", "Sulod sa network reciever")
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            networkChangeListener.networkStateChanged(status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
        }
    }

    companion object {
        lateinit var networkChangeListener: NetworkChangeListener
    }
}