package com.jekro.lesjardindecaro.ui.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jekro.lesjardindecaro.R

class ListProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_page)

        supportFragmentManager.beginTransaction().replace(
            R.id.productListContainer,
            ListProductFragment()
        ).commit()
    }
}