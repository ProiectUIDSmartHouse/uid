package uid.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var lights: TextView;
    private lateinit var temperature: TextView;
    private lateinit var kidsTV: TextView;
    private lateinit var notifications: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)

        lights = findViewById(R.id.lights)
        temperature = findViewById(R.id.temperature)
        kidsTV = findViewById(R.id.kids_tv)
        notifications = findViewById(R.id.notifications)

        lights.setOnClickListener {
            val intent = Intent(this, LightsActivity::class.java)
            startActivity(intent)
        }

        temperature.setOnClickListener {
            val intent = Intent(this, TemperatureActivity::class.java)
            startActivity(intent)
        }

        kidsTV.setOnClickListener {
            val intent = Intent(this, KidsTvActivity::class.java)
            startActivity(intent)
        }

        notifications.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}