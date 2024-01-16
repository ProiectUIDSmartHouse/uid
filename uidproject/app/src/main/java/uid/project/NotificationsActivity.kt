package uid.project

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity

class NotificationsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifications)

        val btnAskForHelp = findViewById<Button>(R.id.btnAskForHelp)
        val btnEmergency = findViewById<Button>(R.id.btnEmergency)

        btnAskForHelp.setOnClickListener {
            showPopup("Help notification was sent!")
        }

        btnEmergency.setOnClickListener {
            showPopup("Emergency reported, all family members were notified")
        }
    }
    private fun showPopup(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}