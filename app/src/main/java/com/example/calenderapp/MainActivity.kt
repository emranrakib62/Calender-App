package com.example.calenderapp

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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
            // Month Header
            val monthLabel = TextView(this).apply {
                text = "\n----------- ${months[i]} ------------\nSun Mon Tue Wed Thu Fri Sat"
                typeface = Typeface.MONOSPACE
                textSize = 16f
                gravity = Gravity.CENTER
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            }
            calendarLayout.addView(monthLabel)

            // Dates Grid
            val calendarText = StringBuilder()
            for (j in 0 until weekday) {
                calendarText.append("    ") // 4 spaces for empty days
            }

            for (day in 1..daysIn[i]) {
                calendarText.append(String.format("%4d", day))
                weekday++
                if (weekday == 7) {
                    calendarText.append("\n")
                    weekday = 0
                }
            }

            val monthContent = TextView(this).apply {
                text = calendarText.toString()
                typeface = Typeface.MONOSPACE
                textSize = 15f
                gravity = Gravity.CENTER
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            }
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
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ -> super.onBackPressed() }
            .setNegativeButton("No", null)
            .show()
    }
}
