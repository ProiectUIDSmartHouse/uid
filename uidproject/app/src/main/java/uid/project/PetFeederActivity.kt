package uid.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PetFeederActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_feeder)

        val editTextGrams = findViewById<EditText>(R.id.editTextGrams)
        val spinnerPets = findViewById<Spinner>(R.id.spinnerPets)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val btnSetFeeder = findViewById<Button>(R.id.btnSetFeeder)

        btnSetFeeder.setOnClickListener {
            val grams = editTextGrams.text.toString().toInt()
            val selectedPet = spinnerPets.selectedItem.toString()

            val hour = timePicker.hour
            val minute = timePicker.minute

            val message = "Time $hour:$minute set to feed $selectedPet with $grams grams."

            showToast(message)
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
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
}