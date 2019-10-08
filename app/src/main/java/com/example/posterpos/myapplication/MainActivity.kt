package com.example.posterpos.myapplication

import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.joinposter.transport.PosterTransport
import com.joinposter.transport.server.PosterDevice
import kotlinx.android.synthetic.main.activity_main.SendBtn

private const val TAG = "PosterTransport"
private const val APP_ID = "272"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        SendBtn.setOnClickListener {
            val connected = PosterTransport.connectedDevices
            connected.forEach {
                it.sendMessage("{\"text\":\"Hello!\"}")
            }
        }

        with(PosterTransport) {
            onMessage = this@MainActivity.onMessage
            onClose = { code, info, initByRemote ->
                Log.d(TAG, "Closed connection: $code $info $initByRemote")
            }
            onError = { error ->
                Log.d(TAG, "Error: ${error.message ?: error.toString()}")
            }
            onFinishedInit = {
                Log.d(TAG, "Library is ready")
            }
            onOpen = {
                hashMapOf("simple" to "test")
            }

            // Initializing library
            init(applicationContext, APP_ID)
        }

        // Make current device visible to Poster terminals
        PosterTransport.start()
    }

    // Tracking what terminal sends to our app
    private val onMessage: (device: PosterDevice, message: String) -> Unit = { device, message ->
        runOnUiThread {
            Toast.makeText(this, "Message: $message\nFrom: ${device.ip}", Toast.LENGTH_SHORT).show()
        }
    }
}
