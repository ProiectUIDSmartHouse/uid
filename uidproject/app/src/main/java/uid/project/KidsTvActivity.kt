package uid.project

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.util.Locale

class KidsTvActivity : ComponentActivity() {

    private lateinit var countdownTextView1: TextView
    private lateinit var countDownTimer1: CountDownTimer
    private lateinit var countdownTextView2: TextView
    private lateinit var countDownTimer2: CountDownTimer
    private lateinit var resetButton1: Button
    private lateinit var resetButton2: Button
    private lateinit var addTenMinutes1: Button
    private lateinit var addTenMinutes2: Button
    private lateinit var resetAll: Button
    private lateinit var turnOffAll: Button
    private var timeLeft1: Long = 0
    private var timeLeft2: Long = 0
    private var disabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kids_tv)

        countdownTextView1 = findViewById(R.id.timer1)
        resetButton1 = findViewById(R.id.reset_1)
        addTenMinutes1 = findViewById(R.id.add_minutes_1)
        countdownTextView2 = findViewById(R.id.timer2)
        resetButton2 = findViewById(R.id.reset_2)
        addTenMinutes2 = findViewById(R.id.add_minutes_2)
        resetAll = findViewById(R.id.reset_all)
        turnOffAll = findViewById(R.id.turn_off_all)

        val initTime1: Long = 27 * 60 * 1000 // 1 hour 30 minutes
        val initTime2: Long = 41 * 60 * 1000
        timeLeft1 = initTime1
        timeLeft2 = initTime2

        // Create a CountDownTimer with the initial time and interval of 1 second
        countDownTimer1 = object : CountDownTimer(initTime1, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft1 = millisUntilFinished
                val timeFormatted = formatTime(millisUntilFinished)
                countdownTextView1.text = timeFormatted
            }

            override fun onFinish() {
                countdownTextView1.text = "00:00:00"
            }
        }

        countDownTimer2 = object : CountDownTimer(initTime2, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft2 = millisUntilFinished
                val timeFormatted = formatTime(millisUntilFinished)
                countdownTextView2.text = timeFormatted
            }

            override fun onFinish() {
                countdownTextView2.text = "00:00:00"
            }
        }

        resetButton1.setOnClickListener {
            resetTimer(1)
        }
        addTenMinutes1.setOnClickListener {
            add10MinutesToTimer(1)
        }
        resetButton2.setOnClickListener {
            resetTimer(2)
        }
        addTenMinutes2.setOnClickListener {
            add10MinutesToTimer(2)
        }
        resetAll.setOnClickListener {
            resetTimer(1)
            resetTimer(2)
        }
        turnOffAll.setOnClickListener {
            if (!disabled) {
                disabled = true
                turnOffAll.setText(R.string.tvs_turned_off)
                resetButton1.isEnabled = false
                resetButton2.isEnabled = false
                addTenMinutes1.isEnabled = false
                addTenMinutes2.isEnabled = false

                resetButton1.setBackgroundResource(R.drawable.disabled_button_layout)
                resetButton2.setBackgroundResource(R.drawable.disabled_button_layout)
                addTenMinutes1.setBackgroundResource(R.drawable.disabled_button_layout)
                addTenMinutes2.setBackgroundResource(R.drawable.disabled_button_layout)

                countDownTimer1.cancel()
                countDownTimer2.cancel()
                countdownTextView1.text = "00:00:00"
                countdownTextView2.text = "00:00:00"
            }else {
                disabled = false
                turnOffAll.setText(R.string.turn_off_tvs)
                resetButton1.isEnabled = true
                resetButton2.isEnabled = true
                addTenMinutes1.isEnabled = true
                addTenMinutes2.isEnabled = true

                resetButton1.setBackgroundResource(R.drawable.rounded_button_layout)
                resetButton2.setBackgroundResource(R.drawable.rounded_button_layout)
                addTenMinutes1.setBackgroundResource(R.drawable.rounded_button_layout)
                addTenMinutes2.setBackgroundResource(R.drawable.rounded_button_layout)

                resetTimer(1)
                resetTimer(2)
            }
        }

        // Start the countdown timer
        countDownTimer1.start()
        countDownTimer2.start()
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

    private fun formatTime(millis: Long): String {
        val hours = (millis / (1000 * 60 * 60)).toInt()
        val minutes = ((millis % (1000 * 60 * 60)) / (1000 * 60)).toInt()
        val seconds = ((millis % (1000 * 60)) / 1000).toInt()

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    // Reset the timer back to 1 hour and 30 minutes
    private fun resetTimer(timerNb: Int) {
        if (timerNb == 1) {
            countDownTimer1.cancel()

            // Set the initial time (1 hour and 30 minutes) in milliseconds
            val initialTimeInMillis: Long = 30 * 60 * 1000 // 1 hour 30 minutes

            // Create a new CountDownTimer with the initial time and interval of 1 second
            countDownTimer1 = object : CountDownTimer(initialTimeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft1 = millisUntilFinished
                    val timeFormatted = formatTime(millisUntilFinished)
                    countdownTextView1.text = timeFormatted
                }

                override fun onFinish() {
                    // Handle the timer finishing (reaching zero) if needed
                    countdownTextView1.text = "00:00:00"
                }
            }

            // Start the new countdown timer
            countDownTimer1.start()
            return;
        }
        countDownTimer2.cancel()

        // Set the initial time (1 hour and 30 minutes) in milliseconds
        val initialTimeInMillis: Long = 45 * 60 * 1000 // 1 hour 30 minutes

        // Create a new CountDownTimer with the initial time and interval of 1 second
        countDownTimer2 = object : CountDownTimer(initialTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft2 = millisUntilFinished
                val timeFormatted = formatTime(millisUntilFinished)
                countdownTextView2.text = timeFormatted
            }

            override fun onFinish() {
                // Handle the timer finishing (reaching zero) if needed
                countdownTextView2.text = "00:00:00"
            }
        }

        // Start the new countdown timer
        countDownTimer2.start()
    }

    // Add 10 minutes to the current timer
    private fun add10MinutesToTimer(timerNb: Int) {
        if (timerNb == 1) {
            timeLeft1 += 10 * 60 * 1000
            countDownTimer1.cancel()

            countDownTimer1 = object : CountDownTimer(timeLeft1, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft1 = millisUntilFinished
                    val timeFormatted = formatTime(millisUntilFinished)
                    countdownTextView1.text = timeFormatted
                }

                override fun onFinish() {
                    countdownTextView1.text = "00:00:00"
                }
            }

            // Start the new countdown timer
            countDownTimer1.start()
            return
        }
        timeLeft2 += 10 * 60 * 1000
        countDownTimer2.cancel()

        countDownTimer2 = object : CountDownTimer(timeLeft2, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft2 = millisUntilFinished
                val timeFormatted = formatTime(millisUntilFinished)
                countdownTextView2.text = timeFormatted
            }

            override fun onFinish() {
                countdownTextView2.text = "00:00:00"
            }
        }

        countDownTimer2.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer1.cancel()
        countDownTimer2.cancel()
    }
}