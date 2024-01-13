package uid.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class TemperatureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.temperature)

        val temperatureSlider: SeekBar = findViewById(R.id.temperatureSlider)
        val temperatureDisplay: TextView = findViewById(R.id.temperatureDisplay)
        val onOffButton: Button = findViewById(R.id.onOffButton)

        temperatureSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                temperatureDisplay.text = "Temperature: ${progress}°C"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

                val selectedTemperature = seekBar.progress
                val message = "Temperature is set on $selectedTemperature degrees"
                showToast(message)

            }
        })
        var isTurnedOn = false
        onOffButton.setOnClickListener {
            if (isTurnedOn) {
                onOffButton.text = "TURN ON"
            } else {
                onOffButton.text = "TURN OFF"
            }
            isTurnedOn = !isTurnedOn
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}