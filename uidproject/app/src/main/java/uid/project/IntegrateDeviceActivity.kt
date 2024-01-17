package uid.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity


class IntegrateDeviceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_integrate_device)

        val deviceAddedTextView = findViewById<TextView>(R.id.textview_device_added)

        val buttons = listOf(
            findViewById<Button>(R.id.button_tv),
            findViewById<Button>(R.id.button_phone),
            findViewById<Button>(R.id.button_lights),
            findViewById<Button>(R.id.button_radio),
            findViewById<Button>(R.id.button_thermostat),
            findViewById<Button>(R.id.button_audio)
        )

        buttons.forEach { button ->
            button.setOnClickListener {

                if (button.id == R.id.button_lights) {
                    openLightsActivity()
                }
                deviceAddedTextView.text = getString(R.string.new_device_added, button.text)
            }
        }
    }

    private fun openLightsActivity() {
        val intent = Intent(this, AddLightsActivity::class.java)
        startActivity(intent)
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
