package com.jekro.lesjardindecaro.ui.cart

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.jekro.lesjardindecaro.R
import kotlinx.android.synthetic.main.activity_validate_cart.*
import java.text.SimpleDateFormat
import java.util.*

class ValidateCartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate_cart)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        var cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, 3)
        datePicker.maxDate = cal.timeInMillis
        cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, 1)
        datePicker.minDate = cal.timeInMillis
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)


        val today  = Calendar.getInstance()
        datePicker.init(
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, _, _, _ ->
            val myDate = SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE).parse(datePicker.dayOfMonth.toString()+"-"+datePicker.month.toString()+"-"+datePicker.year.toString());
            val dayName =  SimpleDateFormat("EEEE", Locale.FRANCE).format(myDate)
            val monthName = SimpleDateFormat("MMMM", Locale.FRANCE).format(myDate)
            val yearName = SimpleDateFormat("YYYY", Locale.FRANCE).format(myDate)
            validateDateTextView.text = "Date choisie : " + dayName + " " + datePicker.dayOfMonth + " " + monthName + " " + yearName
        }

    }
}