package uid.project

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_user)

        val editTextUserName = findViewById<EditText>(R.id.editTextUserName)
        val checkboxThermostat = findViewById<CheckBox>(R.id.checkboxThermostat)
        val checkboxTv = findViewById<CheckBox>(R.id.checkboxTv)
        val checkboxLights = findViewById<CheckBox>(R.id.checkboxLights)
        val checkboxSchedule = findViewById<CheckBox>(R.id.checkboxSchedule)
        val buttonAddUser = findViewById<Button>(R.id.buttonAddUser)

        buttonAddUser.setOnClickListener {
            val userName = editTextUserName.text.toString().trim()
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter a name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedFeatures = mutableListOf<String>()
            if (checkboxThermostat.isChecked) selectedFeatures.add("Thermostat")
            if (checkboxTv.isChecked) selectedFeatures.add("TV")
            if (checkboxLights.isChecked) selectedFeatures.add("Lights")
            if (checkboxSchedule.isChecked) selectedFeatures.add("Schedule")

            val message = "User '$userName' added with rights for: ${selectedFeatures.joinToString()}"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

            checkboxThermostat.isChecked = false
            checkboxTv.isChecked = false
            checkboxLights.isChecked = false
            checkboxSchedule.isChecked = false

            editTextUserName.text.clear()
        }
    }
}
