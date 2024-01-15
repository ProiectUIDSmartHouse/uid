package uid.project

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import uid.project.seekbar.CircularSeekBar

class TemperatureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.temperature)

        val temperatureSlider: CircularSeekBar = findViewById(R.id.circularSeekBar1)
        val temperatureDisplay: TextView = findViewById(R.id.temperatureDisplay)
        val onOffButton: Button = findViewById(R.id.onOffButton)

        temperatureSlider.setOnSeekBarChangeListener(object : CircularSeekBar.OnCircularSeekBarChangeListener {

            override fun onProgressChanged(
                circularSeekBar: CircularSeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                temperatureDisplay.text = "Temperature: ${progress}°C"
            }

            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                val selectedTemperature = seekBar?.progress
                val message = "Temperature is set on $selectedTemperature degrees"
                showToast(message)
            }

            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {

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
            R.id.back_home -> {
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