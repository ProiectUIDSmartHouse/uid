package uid.project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class TroubleshootActivity : ComponentActivity() {
    // Variables for dropdown views
    private lateinit var tvDropdown: LinearLayout
    private lateinit var lightsDropdown: LinearLayout
    private lateinit var thermostatDropdown: LinearLayout
    private lateinit var messageTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_troubleshoot)

        tvDropdown = findViewById(R.id.tv_options)
        lightsDropdown = findViewById(R.id.lights_options)
        thermostatDropdown = findViewById(R.id.thermostat_options)
        messageTextView = findViewById(R.id.message_text_view)

        findViewById<TextView>(R.id.tv_category).setOnClickListener {
            toggleDropdown(tvDropdown)
        }

        findViewById<TextView>(R.id.lights_category).setOnClickListener {
            toggleDropdown(lightsDropdown)
        }

        findViewById<TextView>(R.id.thermostat_category).setOnClickListener {
            toggleDropdown(thermostatDropdown)
        }

        findViewById<TextView>(R.id.tv_bedroom).setOnClickListener {
            onDeviceClick(it)
        }
        findViewById<TextView>(R.id.tv_living_room).setOnClickListener {
            onDeviceClick(it)
        }
        findViewById<TextView>(R.id.tv_kitchen).setOnClickListener {
            onDeviceClick(it)
        }
        findViewById<TextView>(R.id.tv_girl).setOnClickListener {
            onDeviceClick(it)
        }
        findViewById<TextView>(R.id.tv_boy).setOnClickListener {
            onDeviceClick(it)
        }
        findViewById<TextView>(R.id.tv_grandparents).setOnClickListener {
            onDeviceClick(it)
        }

        findViewById<TextView>(R.id.lights_bedroom).setOnClickListener {
            onDeviceClick(it)
        }
        findViewById<TextView>(R.id.lights_living_room).setOnClickListener {
            onDeviceClick(it)
        }
        findViewById<TextView>(R.id.lights_kitchen).setOnClickListener {
            onDeviceClick(it)
        }

        findViewById<TextView>(R.id.thermostat_bedroom).setOnClickListener {
            onDeviceClick(it)
        }
        findViewById<TextView>(R.id.thermostat_living_room).setOnClickListener {
            onDeviceClick(it)
        }
        findViewById<TextView>(R.id.thermostat_kitchen).setOnClickListener {
            onDeviceClick(it)
        }
    }

    private fun toggleDropdown(dropdown: LinearLayout) {
        dropdown.visibility = if (dropdown.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    private fun onDeviceClick(view: View) {
        messageTextView.text = "Device ready for repair"
        messageTextView.visibility = View.VISIBLE
        val parentDropdown = view.parent as LinearLayout
        parentDropdown.visibility = View.GONE
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