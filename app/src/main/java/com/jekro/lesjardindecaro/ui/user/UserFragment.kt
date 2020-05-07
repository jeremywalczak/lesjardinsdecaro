package com.jekro.lesjardindecaro.ui.user

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.User
import com.jekro.lesjardindecaro.module.ModuleInteractor
import com.jekro.lesjardindecaro.mvp.AbsFragment
import com.jekro.lesjardindecaro.ui.home.HomePageActivity
import com.jekro.lesjardindecaro.vibrateClickEffect
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_user_informations.*
import kotlinx.android.synthetic.main.include_user_informations.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class UserFragment : AbsFragment<UserContract.View, UserContract.Presenter>(),
    UserContract.View {

    override val presenter: UserContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun getLayoutId(): Int = R.layout.fragment_user_informations

    override fun displayResult() {
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as HomePageActivity).panierImageView.visibility = View.GONE
        val user = presenter.configurationRepo.getUser()
        user?.let {
            firstname_input.setText(it.firstname)
            name_input.setText(it.name)
            phone_input.setText(it.phone)
            mail_input.setText(it.mail)
        }

        user_validate_button?.setOnClickListener {
            vibrateClickEffect()
            val user = presenter.configurationRepo.getUser()?: User()
            user.firstname = firstname_input.text.toString()
            user.name = name_input.text.toString()
            user.mail = mail_input.text.toString()
            user.phone = phone_input.text.toString()
            presenter.configurationRepo.saveUser(user)
            Snackbar.make(view!!, "Vos informations ont bien été modifiées", Snackbar.LENGTH_LONG).show()
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        (activity as HomePageActivity).panierImageView.visibility = View.VISIBLE
        super.onDestroy()
    }
}