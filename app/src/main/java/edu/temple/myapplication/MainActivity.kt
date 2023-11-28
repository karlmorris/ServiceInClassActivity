import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.temple.myapplication.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_start -> {
                // Call the start method of TimerService
                // You need to start the TimerService first before calling its methods
                // For example: startService(Intent(this, TimerService::class.java))
                return true
            }
            R.id.action_pause -> {
                // Call the pause method of TimerService
                return true
            }
            R.id.action_stop -> {
                // Call the stop method of TimerService
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
