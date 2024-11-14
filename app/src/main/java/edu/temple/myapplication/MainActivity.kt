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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    val handler = Handler(Looper.getMainLooper()) {
        true
    }

    var timerBinder: TimerService.TimerBinder? = null
lateinit var counterDisplay :TextView

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            timerBinder = p1 as TimerService.TimerBinder

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            timerBinder = null

        }
    }

    val timerHandler = Handler(Looper.getMainLooper()){
        counterDisplay.text = it.what.toString()
        true
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        val counterText = findViewById<TextView>(R.id.countDisplay)
        val observer = Observer<Int> { newValue ->
            counterText.text = "$newValue"
        }

        // Observe the LiveData from TimerService
        timerBinder?.counter?.observe(this, observer)

        val btnText = findViewById<Button>(R.id.startButton)
        val numberEditText = findViewById<EditText>(R.id.editTextText)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            val inputText = numberEditText.text.toString()
            val number = inputText.toIntOrNull()

            if (number != null) {
                if (timerBinder?.isRunning == false) {
                    timerBinder?.start(number, handler)
                    btnText.text = "Pause"
                } else {
                    // If the timer is running, pause it and change the button to "Start"
                    timerBinder?.pause()
                    btnText.text = "Start"
                }


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
