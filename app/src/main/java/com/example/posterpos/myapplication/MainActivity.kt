package com.example.posterpos.myapplication

import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.joinposter.transport.PosterTransport
import com.joinposter.transport.server.PosterDevice
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        SendBtn.setOnClickListener {
            PosterTransport.devices.forEach { it.sendMessage("{\"text\":\"Hello!\"}") }
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
        Log.d("Lib", "Message: $message\nFrom: ${device.ip}")
    }

    private val onOpen: () -> String = {
        "{\"action\":\"handshake\"}"
    }
}
