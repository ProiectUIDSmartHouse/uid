// AddLightsActivity.kt
package uid.project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import uid.project.model.Room
import uid.project.model.RoomManager

class AddLightsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lights)

        val editTextRoomName = findViewById<EditText>(R.id.editTextRoomName)
        val buttonAddRoom = findViewById<Button>(R.id.buttonAddRoom)

        buttonAddRoom.setOnClickListener {
            val roomName = editTextRoomName.text.toString().trim()

            if (roomName.isNotEmpty()) {
                RoomManager.addRoom(roomName)
                finish()
            } else {
                editTextRoomName.error = "Room name cannot be empty"
            }
        }
    }
}
