package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var timerBinder : TimerService.TimerBinder? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            timerBinder = p1 as TimerService.TimerBinder
        }

        override fun onServiceDisconnected(p0: ComponentName?) {}

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(Intent(this, TimerService::class.java),
            serviceConnection, BIND_AUTO_CREATE
        )

        findViewById<Button>(R.id.actionStart).setOnClickListener {
            timerBinder?.start(100)
        }

        findViewById<Button>(R.id.actionPause).setOnClickListener {
            timerBinder?.pause()
        }
        
        findViewById<Button>(R.id.actionStop).setOnClickListener {
            timerBinder?.stop()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionStart -> {
                Toast.makeText(this, "Timer is running", Toast.LENGTH_SHORT).show()
                timerBinder?.start(100)
            }

            R.id.actionPause -> {
                Toast.makeText(this, "Timer is on hold", Toast.LENGTH_SHORT).show()
                timerBinder?.pause()
            }

            R.id.actionStop -> {
                Toast.makeText(this, "Timer is off", Toast.LENGTH_SHORT).show()
                timerBinder?.stop()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}