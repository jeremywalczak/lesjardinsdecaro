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

        /*datePicker.setOnTouchListener (object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        /*val myDate = SimpleDateFormat("dd-MM-yyyy").parse(datePicker.dayOfMonth+"-"+datePicker.month+"-"+datePicker.year);
                        val simpleDateFormat = SimpleDateFormat("EEEE")
                        val dayName = simpleDateFormat.format(myDate)*/
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })*/
    }
}