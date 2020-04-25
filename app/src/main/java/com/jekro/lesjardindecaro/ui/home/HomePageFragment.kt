package com.jekro.lesjardindecaro.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.auchan.uikit.module.ModuleInteractor
import com.auchan.uikit.mvp.AbsFragment
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.Category
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.ui.list.ListProductFragment
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_homepage.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.collections.ArrayList

class HomePageFragment : AbsFragment<HomePageContract.View, HomePageContract.Presenter>(),
    HomePageContract.View {

    private val swipeTimer = Timer()

    override fun displayResult(configuration: Configuration) {
        val products = configuration.products
        val categoryFruit = configuration.categories.first { category -> category.id == "24" }
        val categoryLegume = configuration.categories.first { category -> category.id == "25" }
        val categoryEpicerieCave = configuration.categories.first { category -> category.id == "26" }
        configuration.carrousselImageHomePage?.let {
            carrousselHomePageViewPager.adapter = HomePageCarrousselAdapter(context!!, configuration.carrousselImageHomePage)
            initBannerViewPager()
        }

        accountImageView?.setOnClickListener {
            activity!!.drawer_layout.openDrawer(Gravity.LEFT)
        }
        legumesImageView.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent, products.filter { presenter.hashProductCategories[it]!!.contains(categoryLegume)}, presenter.hashProductCategories)
            true
        }
        epicerieImageView.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent, products.filter { presenter.hashProductCategories[it]!!.contains(categoryEpicerieCave)}, presenter.hashProductCategories)
            true
        }
        caveImageView.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent, products.filter { presenter.hashProductCategories[it]!!.contains(categoryEpicerieCave)}, presenter.hashProductCategories)
            true
        }
        fruitsImageView.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent, products.filter { presenter.hashProductCategories[it]!!.contains(categoryFruit)}, presenter.hashProductCategories)
            true
        }
        /*panierImageView?.setOnTouchListener { view, motionEvent -> animateCategoryButton(view, motionEvent)
            true
        }*/
    }

    private fun initBannerViewPager() {
        var page = 0
        val handler = Handler()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Runnable {
                    if (carrousselHomePageViewPager != null) {
                        val numPages: Int = carrousselHomePageViewPager.adapter!!.count
                        page = (page + 1) % numPages
                        carrousselHomePageViewPager.setCurrentItem(page, true)
                    }
                })
            }
        }, 5000, 5000)
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
        Toast.makeText(activity, throwable.message, Toast.LENGTH_LONG).show()
    }

    override fun getLayoutId(): Int = R.layout.fragment_homepage

    override val presenter: HomePageContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.start()
    }

    private fun animateCategoryButton(view: View, motionEvent: MotionEvent, productsFiltered: List<Product>? = null, hashMapProductCategories: HashMap<Product, List<Category>>) {
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
                activity!!.supportFragmentManager.beginTransaction().add(
                    R.id.mainContainer,
                    ListProductFragment.newInstance(ArrayList(productsFiltered), hashMapProductCategories)
                ).addToBackStack(ListProductFragment::class.java.toString()).commit()
            }
        }
    }
}