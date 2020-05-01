package com.jekro.lesjardindecaro.ui

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.auchan.uikit.module.ModuleInteractor
import com.jekro.lesjardindecaro.mvp.AbsFragment
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.load
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.ui.home.HomePageActivity
import com.jekro.lesjardindecaro.ui.home.HomePageContract
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class DetailFragment : AbsFragment<HomePageContract.View, HomePageContract.Presenter>(),
HomePageContract.View {

    override fun displayResult(configuration: Configuration) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val product = arguments?.getParcelable<Product>(PRODUCT)
        val shouldMoveCartButton = arguments?.getBoolean(SHOULD_MOVE_CART_BUTTON)
        product?.let {
            detailProduitImageView?.load("http://lejardindecaro.fr${product.image.url}", placeholder = R.drawable.logo_jardin_caro)
            descriptionTextView.text = product.description
            titleTextView.text = product.title
        }
        if (!product?.unity.isNullOrEmpty()) {
            uniteTextView.visibility = View.VISIBLE
            uniteTextView.text = product?.unity
        }

        priceValueTextView.text = "${String.format("%.2f", product!!.price.fractional.toFloat() / 100)} €"
        if (!product.cat.isNullOrEmpty())
            categoryValueView.text = product.cat
        else
            categoryTextView.visibility = View.GONE

        product_number.setText(product!!.defaultQuantity.toString())

        var numberProduct = product_number.text.toString().toInt()
        minus?.setOnTouchListener { view, motionEvent ->
            animateButton(view, motionEvent) {
                if ((product?.unity.isNullOrEmpty() && numberProduct > 1) ||
                    (!product?.unity.isNullOrEmpty() && numberProduct > 100)) {
                    numberProduct -= if (product?.unity.isNullOrEmpty()) 1 else 100
                    product_number.setText(numberProduct.toString())
                }
            }

            true
        }
        plus?.setOnTouchListener { view, motionEvent ->
            animateButton(view, motionEvent) {numberProduct += if (product?.unity.isNullOrEmpty()) 1 else 100
                product_number.setText(numberProduct.toString())}

            true
        }
        if (shouldMoveCartButton != null && shouldMoveCartButton)
            (activity as HomePageActivity).moveCart()
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
        const val SHOULD_MOVE_CART_BUTTON = "SHOULD_MOVE_CART_BUTTON"
        fun newInstance(product: Product, shouldMoveCartButton: Boolean) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PRODUCT, product)
                    putBoolean(SHOULD_MOVE_CART_BUTTON, shouldMoveCartButton)
                }
            }
    }
}