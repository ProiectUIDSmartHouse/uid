package uid.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uid.project.adapters.RoomAdapter

import uid.project.model.RoomManager

class LightsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lights)

        val recyclerViewRooms: RecyclerView = findViewById(R.id.recyclerViewRooms)
        recyclerViewRooms.layoutManager = LinearLayoutManager(this)

        val roomList = RoomManager.roomList
        val roomAdapter = RoomAdapter(roomList)
        recyclerViewRooms.adapter = roomAdapter

        val buttonTurnOnAll: Button = findViewById(R.id.buttonTurnOnAll)
        val buttonTurnOffAll: Button = findViewById(R.id.buttonTurnOffAll)

        buttonTurnOnAll.setOnClickListener {
            roomList.forEach { room ->
                room.isOn = true
            }
            roomAdapter.notifyDataSetChanged()
        }

        buttonTurnOffAll.setOnClickListener {
            roomList.forEach { room ->
                room.isOn = false
            }
            roomAdapter.notifyDataSetChanged()
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

}