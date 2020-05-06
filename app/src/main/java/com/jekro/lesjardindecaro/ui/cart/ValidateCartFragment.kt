package com.jekro.lesjardindecaro.ui.cart

import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.module.ModuleInteractor
import com.jekro.lesjardindecaro.mvp.AbsFragment
import com.jekro.lesjardindecaro.ui.home.HomePageContract
import kotlinx.android.synthetic.main.fragment_validate_cart.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class ValidateCartFragment : AbsFragment<ValidateCartContract.View, ValidateCartContract.Presenter>(),
    ValidateCartContract.View{

    override val presenter: ValidateCartContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun getLayoutId(): Int = R.layout.fragment_validate_cart

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, 3)
        datePicker.maxDate = cal.timeInMillis
        cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, 1)
        datePicker.minDate = cal.timeInMillis
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

        cart_validate_button?.setOnClickListener {

        }
    }
    override fun displayResult(cart: Cart) {
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }
}