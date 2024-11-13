package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val handler = Handler(Looper.getMainLooper()) {
        true
    }

    var timerBinder: TimerService.TimerBinder? = null

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            timerBinder = p1 as TimerService.TimerBinder
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            timerBinder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        val numberEditText = findViewById<EditText>(R.id.editTextText)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            val inputText = numberEditText.text.toString()  // Get the text from the EditText
            val number = inputText.toIntOrNull()  // Convert the text to an integer (or null if not valid)

            if (number != null) {
                timerBinder?.start(number, handler)  // Pass the integer to the service
            } else {
                // Handle invalid input
                numberEditText.error = "Please enter a valid number"
            }
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            timerBinder?.pause()
        }

        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder?.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}
