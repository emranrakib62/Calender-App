package com.example.calenderapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import soup.neumorphism.NeumorphCardView
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year

class MainActivity : AppCompatActivity() {

    private lateinit var calendarLayout: LinearLayout
    private lateinit var inputYearEditText: EditText
    private lateinit var startButton: NeumorphCardView
    private lateinit var resultText: TextView

    private val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    private val daysInMonth = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputYearEditText = findViewById(R.id.inputYearEditText)
        startButton = findViewById(R.id.startButton)
        resultText = findViewById(R.id.resultText)
        calendarLayout = findViewById(R.id.calendarLayout)

        startButton.setOnClickListener {
            val yearStr = inputYearEditText.text.toString()
            if (yearStr.isEmpty()) {
                Toast.makeText(this, "Please enter a year", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val year = yearStr.toInt()
            displayCalendar(year)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayCalendar(year: Int) {
        calendarLayout.removeAllViews()

        val isLeap = Year.of(year).isLeap
        val daysIn = daysInMonth.copyOf()
        if (isLeap) daysIn[1] = 29

        resultText.text = if (isLeap)
            "$year is a leap year"
        else
            "$year is not a leap year"

        var weekday = getFirstDayOfYear(year)

        for (i in months.indices) {
            val monthLabel = TextView(this)
            monthLabel.text = "\n--------- ${months[i]} ---------\nSun Mon Tue Wed Thu Fri Sat"
            calendarLayout.addView(monthLabel)

            val calendarText = StringBuilder()
            for (j in 0 until weekday) {
                calendarText.append("    ")
            }

            for (day in 1..daysIn[i]) {
                calendarText.append(String.format("%4d", day))
                weekday++
                if (weekday == 7) {
                    calendarText.append("\n")
                    weekday = 0
                }
            }

            val monthContent = TextView(this)
            monthContent.text = calendarText.toString()
            calendarLayout.addView(monthContent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFirstDayOfYear(year: Int): Int {
        return when (LocalDate.of(year, 1, 1).dayOfWeek) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.MONDAY -> 1
            DayOfWeek.TUESDAY -> 2
            DayOfWeek.WEDNESDAY -> 3
            DayOfWeek.THURSDAY -> 4
            DayOfWeek.FRIDAY -> 5
            DayOfWeek.SATURDAY -> 6
        }
    }
}
