package com.example.signalr

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState


class MainActivity : AppCompatActivity(), NetworkChangeListener {

    private val hubConnection: HubConnection =
        HubConnectionBuilder.create("URL_SOCKET").build()

    private var networkReceiver: BroadcastReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkReceiver = NetworkChangeReceiver()

        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(networkReceiver, intentFilter)

        (application as App).initializeNetworkListener(this)

    }

    private fun connectHub() {
        this.hubConnection.serverTimeout = 900000
        this.hubConnection.start().blockingAwait()
        this.hubConnection.invoke(Void::class.java, "JoinGroup", "RUTA-USER")
    }

    fun onHubConnection() {
        if (this.hubConnection.connectionState == HubConnectionState.CONNECTED) {
            this.hubConnection.on(
                "ReceiveMessage",
                { message ->
                    if (message == "") {
                        //Mostrar mensaje de que existe una nueva BD
                    }
                },
                String::class.java
            )
        }
    }

    private fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkChanges()
    }

    override fun networkStateChanged(connect: Boolean) {
        if (connect) {
            Toast.makeText(this, "Connect", Toast.LENGTH_LONG).show()
            if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) {
                //connectHub()
            }
        } else {
            Toast.makeText(this, "No Connect", Toast.LENGTH_LONG).show()
            if (hubConnection.connectionState == HubConnectionState.CONNECTED) {
                hubConnection.stop()
            }
        }
    }
}