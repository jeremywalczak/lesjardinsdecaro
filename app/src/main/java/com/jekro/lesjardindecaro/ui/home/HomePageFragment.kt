package com.jekro.lesjardindecaro.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.auchan.uikit.module.ModuleInteractor
import com.auchan.uikit.mvp.AbsFragment
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.ui.list.ListProductActivity
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_homepage.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.collections.ArrayList

class HomePageFragment : AbsFragment<HomePageContract.View, HomePageContract.Presenter>(),
    HomePageContract.View {

    override fun displayResult(configuration: Configuration) {
        val products = configuration.products

        initBannerViewPager(configuration)

        accountImageView?.setOnClickListener {
            activity!!.drawer_layout.openDrawer(Gravity.LEFT)
        }
        legumesImageView.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent, products.filter { it.category ==  "legume"})
            true
        }
        epicerieImageView.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent, products.filter { it.category ==  "epicerie"})
            true
        }
        caveImageView.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent, products.filter { it.category ==  "cave"})
            true
        }
        fruitsImageView.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent, products.filter { it.category ==  "fruit"})
            true
        }
        panierImageView.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent)
            true
        }
    }

    private fun initBannerViewPager(configuration: Configuration) {
        carrousselHomePageViewPager.adapter = HomePageCarrousselAdapter(context!!, configuration.carrousselImageHomePage)
        mainVpIndicator.setViewPager(carrousselHomePageViewPager)
        var page = 0

        val handler = Handler()
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Runnable {
                    val numPages: Int = carrousselHomePageViewPager.adapter!!.count
                    page = (page + 1) % numPages
                    carrousselHomePageViewPager.setCurrentItem(page, true)
                })
            }
        }, 5000, 5000)
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    override fun getLayoutId(): Int = R.layout.fragment_homepage

    override val presenter: HomePageContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.start()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun animateCategoryButton(view: View, motionEvent: MotionEvent, productsFiltered: List<Product>? = null) {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            view.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            view.alpha = 0.9F
            view.scaleX = 0.9F
            view.scaleY = 0.9F
        }
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            view.alpha = 1F
            view.scaleX = 1F
            view.scaleY = 1F
            if (!productsFiltered.isNullOrEmpty()) {
                val intent = Intent(activity, ListProductActivity::class.java)
                intent.putParcelableArrayListExtra(PRODUCTS, ArrayList(productsFiltered))
                startActivity(intent)
            }
        }
    }

    companion object {
        const val PRODUCTS = "PRODUCTS"
    }
}