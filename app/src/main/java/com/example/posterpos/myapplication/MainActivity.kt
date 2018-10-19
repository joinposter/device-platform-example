package com.example.posterpos.myapplication

import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
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

        PosterTransport.init(applicationContext, "272")
        //We want to track what terminal sends to us
        PosterTransport.onMessage = this.onMessage
        //Make current device visible to Poster terminal
        PosterTransport.start()
    }

    private val onMessage: (device: PosterDevice, message: String) -> Unit = { device, message ->
        runOnUiThread {
            Toast.makeText(this, "Message: $message\nTo: ${device.ip}", Toast.LENGTH_SHORT).show()
        }
    }
}
