package com.example.signalr

interface NetworkChangeListener {
    fun networkStateChanged(connect: Boolean)
}