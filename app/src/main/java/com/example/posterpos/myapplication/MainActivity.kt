package com.example.posterpos.myapplication

import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonParser
import com.joinposter.transport.PosterTransport
import com.joinposter.transport.server.PosterDevice
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Contains IDs of connected Terminals
    private val terminals = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        SendBtn.setOnClickListener {
            terminals.forEach { PosterTransport.sendMessage(it, "{\"text\":\"Hello!\"}") }
        }
        Log.e("MA", PosterTransport.toString())
        PosterTransport.with(applicationContext, "272")
        PosterTransport.onOpen = this.onOpen
        PosterTransport.onMessage = this.onMessage
        PosterTransport.start()
    }

    private val onMessage: (device: PosterDevice, message: String) -> Unit = { device, message ->
        runOnUiThread {
            Toast.makeText(this, "Message: $message\nTo: ${device.ip}", Toast.LENGTH_SHORT).show()
        }
        terminals.add(JsonParser().parse(message).asJsonObject["terminalId"].asString)
        Log.d("Lib", "Message: $message\nFrom: ${device.ip}")
    }

    private val onOpen: () -> String = {
        "{\"action\":\"handshake\"}"
    }
}
