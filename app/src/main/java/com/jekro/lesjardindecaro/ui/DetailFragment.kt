package com.jekro.lesjardindecaro.ui

import android.os.Bundle
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.auchan.uikit.module.ModuleInteractor
import com.auchan.uikit.mvp.AbsFragment
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.load
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.ui.home.HomePageActivity
import com.jekro.lesjardindecaro.ui.home.HomePageContract
import com.jekro.lesjardindecaro.ui.list.ListProductFragment
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_homepage.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class DetailFragment : AbsFragment<HomePageContract.View, HomePageContract.Presenter>(),
HomePageContract.View {

    override fun displayResult(configuration: Configuration) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val product = arguments?.getParcelable<Product>(PRODUCT)
        product?.let {
            detailProduitImageView?.load(product.image)
            descriptionTextView.text = product.description
        }
        if (!product?.unite.isNullOrEmpty()) {
            uniteTextView.visibility = View.VISIBLE
            uniteTextView.text = product?.unite
            product_number.setText("100")
        } else {
            product_number.setText("1")
        }

        var numberProduct = product_number.text.toString().toInt()
        minus?.setOnTouchListener { view, motionEvent ->
            animateButton(view, motionEvent) {
                if ((product?.unite.isNullOrEmpty() && numberProduct > 1) ||
                    (!product?.unite.isNullOrEmpty() && numberProduct > 100)) {
                    numberProduct -= if (product?.unite.isNullOrEmpty()) 1 else 100
                    product_number.setText(numberProduct.toString())
                }
            }

            true
        }
        plus?.setOnTouchListener { view, motionEvent ->
            animateButton(view, motionEvent) {numberProduct += if (product?.unite.isNullOrEmpty()) 1 else 100
                product_number.setText(numberProduct.toString())}

            true
        }
    }

    private fun animateButton(view: View, motionEvent: MotionEvent, operation: (() -> Unit)?) {
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
            operation?.invoke()
        }
    }


    override fun setRequesting(requesting: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.fragment_detail

    override val presenter: HomePageContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    companion object {
        const val PRODUCT = "PRODUCT"
        fun newInstance(product: Product) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PRODUCT, product)
                }
            }
    }
}