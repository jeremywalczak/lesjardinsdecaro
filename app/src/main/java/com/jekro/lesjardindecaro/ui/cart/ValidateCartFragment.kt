package com.jekro.lesjardindecaro.ui.cart

import android.os.Bundle
import android.os.Handler
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.google.android.material.snackbar.Snackbar
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.model.User
import com.jekro.lesjardindecaro.module.ModuleInteractor
import com.jekro.lesjardindecaro.mvp.AbsFragment
import com.jekro.lesjardindecaro.vibrateClickEffect
import kotlinx.android.synthetic.main.fragment_validate_cart.*
import kotlinx.android.synthetic.main.include_user_informations.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class ValidateCartFragment : AbsFragment<ValidateCartContract.View, ValidateCartContract.Presenter>(),
    ValidateCartContract.View{

    override val presenter: ValidateCartContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()
    private val swipeTimer = Timer()

    override fun getLayoutId(): Int = R.layout.fragment_validate_cart

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = presenter.configurationRepo.getUser()
        user?.let {
            firstname_input.setText(it.firstname)
            name_input.setText(it.name)
            phone_input.setText(it.phone)
            mail_input.setText(it.mail)
        }

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
            updateDate()
        }

        timePicker.setOnTimeChangedListener { _, hour, minute ->
            updateDate()
        }

        updateDate()

        cart_validate_button?.setOnClickListener {
            vibrateClickEffect()
            val user = presenter.configurationRepo.getUser()?:User()
            user.firstname = firstname_input.text.toString()
            user.name = name_input.text.toString()
            user.mail = mail_input.text.toString()
            user.phone = phone_input.text.toString()
            presenter.configurationRepo.saveUser(user)
            val cart = presenter.configurationRepo.getCart()
            cart?.comment = comment_user_informations.text.toString()
            cart?.dateOrder = validateDateTextView.text.toString()
            presenter.configurationRepo.saveCart(cart!!)
            sendMailToYacaro()
        }
    }

    private fun sendMailToYacaro() {
        val myDate = SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE).parse(Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
                +"-"+(Calendar.getInstance().get(Calendar.MONTH)+1).toString()+"-"+Calendar.getInstance().get(Calendar.YEAR).toString())
        val dayName =  SimpleDateFormat("EEEE", Locale.FRANCE).format(myDate)
        val monthName = SimpleDateFormat("MMMM", Locale.FRANCE).format(myDate)
        val yearName = SimpleDateFormat("YYYY", Locale.FRANCE).format(myDate)

        val user = presenter.configurationRepo.getUser()!!
        val order = presenter.configurationRepo.getCart()!!
        var body = "ID : " +  order.id + "\n"
        body +=  "NOM : " +  user.name + "\n"
        body +=  "PRENOM : " +  user.firstname + "\n"
        body +=  "EMAIL : " +  user.mail + "\n"
        body +=  "TELEPHONE : " +  user.phone + "\n"
        body +=  "RETRAIT LE : " +  order.dateOrder + "\n"
        body +=  "NB PRODUITS : " +  order.productsQuantity.keys.size + "\n"
        body +=  "MONTANT : ${String.format("%.2f",order.amountTotal)}€\n"
        body +=  "COMMENTAIRE : " +  order.comment + "\n"
        body +=  "\n\n"

        order.productsQuantity.keys.forEach { product ->
            val quantity = if (product.unity.isNullOrEmpty()) order.productsQuantity[product]!! else order.productsQuantity[product]!!/100
            val divider = if (product.unity.isNullOrEmpty()) 100 else 1000
            body +=  "Produit : " +  product.title + " | Prix : ${String.format("%.2f",product.price.fractional.toFloat() / 100)}€ | Quantité : " + order.productsQuantity[product] + product.unity + " | Total : " + "${String.format("%.2f",(product.price.fractional.toFloat() / divider) * quantity)}€ \n\n"
        }


        BackgroundMail.newBuilder(activity!!)
            .withUsername("lesjardinsdecaro@gmail.com")
            .withPassword("jeje200889")
            .withSenderName("Application Android Yacaro")
            .withMailTo("jeremwalczak@gmail.com")
            .withType(BackgroundMail.TYPE_PLAIN)
            .withSubject("Commande du " + dayName + " " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " + monthName + " " + yearName)
            .withBody(body)
            .withSendingMessage("Envoie de votre commande en cours...")
            .withOnSuccessCallback(object : BackgroundMail.OnSendingCallback {
                override fun onSuccess() {
                    Snackbar.make(view!!, "Votre commande a bien été envoyée :D", Snackbar.LENGTH_LONG).show()
                    presenter.configurationRepo.saveCart(null)
                    val handler = Handler()
                    swipeTimer.schedule(object : TimerTask() {
                        override fun run() {
                            handler.post(Runnable {
                                activity?.finish()
                            })
                        }
                    }, 3000, 3000)

                }

                override fun onFail(e: Exception) {
                    Snackbar.make(view!!, "Un problème est survenue, veuillez ressayer ou nous contacter si le problème persiste", Snackbar.LENGTH_LONG).show()
                }
            })
            .send()
    }
    override fun displayResult(cart: Cart) {
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    private fun updateDate() {
        val myDate = SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE).parse(datePicker.dayOfMonth.toString()+"-"+(datePicker.month+1).toString()+"-"+datePicker.year.toString());
        val dayName =  SimpleDateFormat("EEEE", Locale.FRANCE).format(myDate)
        val monthName = SimpleDateFormat("MMMM", Locale.FRANCE).format(myDate)
        val yearName = SimpleDateFormat("YYYY", Locale.FRANCE).format(myDate)
        val hour =  timePicker.currentHour
        val min = timePicker.currentMinute
        // display format of time
        val msg = " $hour : $min"
        validateDateTextView.text =  "Date choisie : " + dayName + " " + datePicker.dayOfMonth + " " + monthName + " " + yearName + " à   $msg"

    }
}