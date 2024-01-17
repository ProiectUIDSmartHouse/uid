package uid.project

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uid.project.adapters.EventAdapter
import uid.project.model.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleActivity : ComponentActivity() {
    private lateinit var calendarView: CalendarView
    private lateinit var recyclerView: RecyclerView
    private lateinit var addEventButton: Button

    private lateinit var eventAdapter: EventAdapter

    private val events = mutableListOf<Event>()
    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule)

        events.add(Event(1, "Meeting with Client - Dad", System.currentTimeMillis(), "10:00"))
        events.add(Event(2, "Dinner with Family", System.currentTimeMillis() + 86400000, "18:00"))
        events.add(Event(3, "Wedding anniversary", System.currentTimeMillis() + 2 * 86400000, "00:00"))
        events.add(Event(3, "Physics Project - Teenager", System.currentTimeMillis() + 4 * 86400000, "13:00"))

        calendarView = findViewById(R.id.calendarView)
        recyclerView = findViewById(R.id.recyclerView)
        addEventButton = findViewById(R.id.add_event_button)

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventAdapter(events)
        recyclerView.adapter = eventAdapter

        updateRecyclerView(selectedDate)

        // Set CalendarView listener
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }

            // Update RecyclerView with events for the selected date
            updateRecyclerView(selectedDate)
        }

        addEventButton.setOnClickListener {
            showAddEventDialog2()
        }
    }

    private fun updateRecyclerView(selectedDate: Calendar) {
        // Filter events for the selected date and update the RecyclerView
        val filteredEvents = events.filter { event ->
            val eventDate = Calendar.getInstance()
            eventDate.timeInMillis = event.date
            eventDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                    eventDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                    eventDate.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)
        }
        eventAdapter = EventAdapter(filteredEvents)
        recyclerView.adapter = eventAdapter
    }

    private fun showAddEventDialog2() {
        Log.d("dialog_schedule", "show dialog")
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_add_event, null)
        Log.d("dialog_schedule", view.toString())

        val titleEditText: EditText = view.findViewById(R.id.titleEditText)
        val timeEditText: EditText = view.findViewById(R.id.timeEditText)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDate.timeInMillis

        // Set up the default values in the dialog
        titleEditText.setText("")
        timeEditText.setText(SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time))
        Log.d("dialog_schedule", timeEditText.text.toString())

        // Build the AlertDialog
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Add Event")
            .setView(view)
            .setPositiveButton("Add") { _, _ ->
                val title = titleEditText.text.toString()
                val time = timeEditText.text.toString()

                // Create a new Event object and add it to the events list
                val newEvent = Event(
                    id = events.size.toLong() + 1,
                    title = title,
                    date = calendar.timeInMillis,
                    time = time
                )
                events.add(newEvent)

                // Update the RecyclerView
                updateRecyclerView(calendar)
            }
            .setNegativeButton("Cancel", null)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
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