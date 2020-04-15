package com.jekro.lesjardindecaro.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.auchan.uikit.module.ModuleInteractor
import com.auchan.uikit.mvp.AbsFragment
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.Products
import com.jekro.lesjardindecaro.ui.list.ListProductActivity
import kotlinx.android.synthetic.main.fragment_homepage.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class HomePageFragment : AbsFragment<HomePageContract.View, HomePageContract.Presenter>(),
    HomePageContract.View {

    override fun displayResult(listCoupons: List<Products>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRequesting(requesting: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.fragment_homepage

    override val presenter: HomePageContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //presenter.start()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        legumesImageView?.setOnClickListener {
            startActivity(Intent(activity, ListProductActivity::class.java))
        }
    }
}