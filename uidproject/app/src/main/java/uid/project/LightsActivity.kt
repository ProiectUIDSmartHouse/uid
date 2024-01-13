package uid.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uid.project.adapters.RoomAdapter
import uid.project.model.Room

class LightsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lights)

        val recyclerViewRooms: RecyclerView = findViewById(R.id.recyclerViewRooms)
        recyclerViewRooms.layoutManager = LinearLayoutManager(this)

        val roomList = generateRoomList()
        val roomAdapter = RoomAdapter(roomList)
        recyclerViewRooms.adapter = roomAdapter
    }


    private fun generateRoomList(): List<Room> {
        // Implement this method to create and return a list of Room objects
        // You can retrieve this data from a database, API, or any other source
        val rooms = mutableListOf(
            Room("Living Room", false),
            Room("Bedroom", true),
            Room("Kitchen", false)
            // Add more rooms as needed
        )
        return rooms
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