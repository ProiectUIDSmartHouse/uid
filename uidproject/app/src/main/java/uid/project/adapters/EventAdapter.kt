package uid.project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uid.project.R
import uid.project.model.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EventAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventTitle: TextView = itemView.findViewById(R.id.event_title)
        private val eventDate: TextView = itemView.findViewById(R.id.event_date)
        private val eventTime: TextView = itemView.findViewById(R.id.event_time)

        fun bind(event: Event) {
            eventTitle.text = event.title
            eventTime.text = event.time
            eventDate.text = convertMillisToDateString(event.date)
        }

        private fun convertMillisToDateString(millis: Long): String {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance().apply {
                timeInMillis = millis
            }
            return dateFormat.format(calendar.time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        // Inflate the item layout and return a ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // Bind data to the ViewHolder
        holder.bind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }


}
