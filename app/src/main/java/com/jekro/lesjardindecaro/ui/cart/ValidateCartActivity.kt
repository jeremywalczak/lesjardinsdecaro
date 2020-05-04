package com.jekro.lesjardindecaro.ui.cart

import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.jekro.lesjardindecaro.R
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
    }
}