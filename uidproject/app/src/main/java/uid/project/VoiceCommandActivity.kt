package uid.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class VoiceCommandActivity : ComponentActivity() {


    private lateinit var btnRecord: Button
    private lateinit var tvRecording: TextView
    private lateinit var btnExecuteCommand: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_command)

        btnRecord = findViewById(R.id.btnRecord)
        tvRecording = findViewById(R.id.tvRecording)
        btnExecuteCommand = findViewById(R.id.btnExecuteCommand)

        btnRecord.setOnTouchListener { _, event ->
            handleRecordButtonTouch(event)
        }
    }

    private fun handleRecordButtonTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                btnRecord.setBackgroundResource(R.drawable.record_button_bg)
                tvRecording.visibility = View.VISIBLE
                tvRecording.text = "Recording"
            }
            MotionEvent.ACTION_UP -> {
                btnRecord.setBackgroundResource(R.drawable.record_button_bg)
                tvRecording.visibility = View.GONE
                btnExecuteCommand.visibility = View.VISIBLE
            }
        }
        return true
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
}