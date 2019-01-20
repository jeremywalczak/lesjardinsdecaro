package com.jekro.lesjardindecaro.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jekro.lesjardindecaro.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(
            R.id.mainContainer,
            HomePageFragment()
        ).commit()
    }
}
