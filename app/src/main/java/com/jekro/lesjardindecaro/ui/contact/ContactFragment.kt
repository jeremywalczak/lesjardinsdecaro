package com.jekro.lesjardindecaro.ui.contact

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.jekro.lesjardindecaro.Constants.Companion.PERMISSION_PHONE_REQUEST_CODE
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.module.ModuleInteractor
import com.jekro.lesjardindecaro.mvp.AbsFragment
import com.jekro.lesjardindecaro.showDialogWithConfirm
import com.jekro.lesjardindecaro.ui.home.HomePageActivity
import com.jekro.lesjardindecaro.vibrateClickEffect
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_contact.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.net.URLEncoder
import java.util.*

class ContactFragment : AbsFragment<ContactContract.View, ContactContract.Presenter>(),
    ContactContract.View {

    override val presenter: ContactContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun getLayoutId(): Int = R.layout.fragment_contact

    override fun displayResult() {
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as HomePageActivity).panierImageView.visibility = View.GONE
        store_details_action_phone.setOnClickListener {
            call()
        }
        store_details_action_itinerary?.setOnClickListener {
            map()
        }
        store_details_action_email?.setOnClickListener {
            mail()
        }
    }

    override fun onDestroy() {
        (activity as HomePageActivity).panierImageView.visibility = View.VISIBLE
        super.onDestroy()
    }

    private fun mail() {
        vibrateClickEffect()
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:contact@lejardindecaro.fr")
        val resolveInfo = activity!!.packageManager.queryIntentActivities(intent, 0)

        if (resolveInfo.size > 0) {
            ContextCompat.startActivity(activity!!, intent, null)
        } else {
            Snackbar.make(view!!, "Aucune application de mail n'est paramétré", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun call() {
        vibrateClickEffect()
        if (!hasPermission(
                activity!!,
                PERMISSION_PHONE_REQUEST_CODE,
                Manifest.permission.CALL_PHONE
            )
        ) {
            return
        }

        val intent = Intent(
            Intent.ACTION_CALL,
            Uri.parse("tel:0562794920")
        )
        val resolveInfo = activity!!.packageManager.queryIntentActivities(intent, 0)

        if (resolveInfo.size > 0) {
            ContextCompat.startActivity(activity!!, intent, null)
        }
    }

    private fun map() {
        vibrateClickEffect()
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:43.780706, 1.360502")
        )
        val resolveInfo = activity!!.packageManager.queryIntentActivities(intent, 0)

        if (resolveInfo.size > 0) {
            ContextCompat.startActivity(activity!!, intent, null)
        }
    }


    private fun hasPermission(
        activity: Activity,
        callbackId: Int,
        vararg permissionIds: String
    ): Boolean {
        var permissions = true
        for (id in permissionIds) {
            permissions = permissions
                    && ContextCompat.checkSelfPermission(
                activity, id
            ) == PackageManager.PERMISSION_GRANTED
        }

        return if (!permissions) {
            ActivityCompat.requestPermissions(activity, permissionIds, callbackId)
            false
        } else {
            true
        }
    }
}

