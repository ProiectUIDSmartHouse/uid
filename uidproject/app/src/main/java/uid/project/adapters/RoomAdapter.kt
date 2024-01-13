package uid.project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uid.project.R
import uid.project.model.Room

class RoomAdapter(private val roomList: List<Room>) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        holder.textViewRoomName.text = room.name
        holder.switchRoom.isChecked = room.isOn

        holder.switchRoom.setOnCheckedChangeListener { _, isChecked ->
            // Handle switch state change
            room.isOn = isChecked
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewRoomName: TextView = itemView.findViewById(R.id.textViewRoomName)
        val switchRoom: Switch = itemView.findViewById(R.id.switchRoom)
    }
}
