package com.jekro.lesjardindecaro.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.jekro.lesjardindecaro.module.ModuleInteractor
import com.jekro.lesjardindecaro.Constants
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.Category
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.mvp.AbsFragment
import com.jekro.lesjardindecaro.ui.DetailFragment
import com.jekro.lesjardindecaro.ui.list.ListProductFragment
import com.jekro.lesjardindecaro.vibrateClickEffect
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_homepage.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.collections.ArrayList


class HomePageFragment : AbsFragment<HomePageContract.View, HomePageContract.Presenter>(),
    HomePageContract.View {

    private val swipeTimer = Timer()

    override fun getContentContainerId(): Int? = R.id.homeContainer

    override fun getInitErrorContainerId(): Int? = R.id.error_container

    override fun getLoadingContainerId(): Int? = R.id.loading_container

    override fun getCartView(): Int? = R.id.panierImageView

    override fun getLayoutId(): Int = R.layout.fragment_homepage

    override val presenter: HomePageContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun displayResult(configuration: Configuration) {
        /*BackgroundMail.newBuilder(activity!!)
            .withUsername("lesjardinsdecaro@gmail.com")
            .withPassword("jeje200889")
            .withSenderName("Application Android Yacaro")
            .withMailTo("dav.bastien@gmail.com")
            .withType(BackgroundMail.TYPE_PLAIN)
            .withSubject("Test")
            .withBody("Vegeta est naze")
            .withSendingMessage("hello")
            .withOnSuccessCallback(object : OnSendingCallback {
                override fun onSuccess() {
                    // do some magic
                    Toast.makeText(activity, "nice :D", Toast.LENGTH_LONG).show()
                }

                override fun onFail(e: Exception) {
                    // do some magic
                    Toast.makeText(activity, "Oooooh :(", Toast.LENGTH_LONG).show()
                }
            })
            .send()*/



        val products = configuration.products.filter { it.status == "1" }
        val categoryFruit = configuration.categories.first { category -> category.id == "24" }
        val categoryLegume = configuration.categories.first { category -> category.id == "25" }
        val categoryEpicerieCave = configuration.categories.first { category -> category.id == "26" }
        val promos = configuration.products.filter { product -> !product.reduce.isNullOrEmpty() }
        if (!promos.isNullOrEmpty()) {
            val adapter = HomePageCarrousselAdapter(context!!, promos)
            adapter?.onItemClick = { product ->
                activity!!.supportFragmentManager.beginTransaction().add(
                    R.id.mainContainer,
                    DetailFragment.newInstance(product, true)
                ).addToBackStack(DetailFragment::class.java.toString()).commit()

            }
            carrousselHomePageViewPager.adapter = adapter
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

        setState(Constants.CONTENT)
    }

    override fun setRequesting(requesting: Boolean) {
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

    override fun displayInitError(throwable: Throwable) {
        activity!!.findViewById<AppCompatButton>(R.id.retry_button)?.setOnClickListener {
            presenter.retry()
        }
        setState(Constants.ERROR)
        Toast.makeText(activity, throwable.message, Toast.LENGTH_LONG).show()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.start()
    }

    override fun setInitRequesting(requesting: Boolean) {
        if (requesting) {
            setState(Constants.REQUESTING)
        }
    }

    override fun displayError(throwable: Throwable) {
        displayInitError(throwable)
    }

    private fun animateCategoryButton(view: View, motionEvent: MotionEvent, productsFiltered: List<Product>? = null, hashMapProductCategories: HashMap<Product, List<Category>>) {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            vibrateClickEffect()
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