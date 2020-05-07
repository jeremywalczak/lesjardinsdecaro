package com.jekro.lesjardindecaro.ui.detail

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.jekro.lesjardindecaro.module.ModuleInteractor
import com.google.android.material.snackbar.Snackbar
import com.jekro.lesjardindecaro.mvp.AbsFragment
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.load
import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.ui.home.HomePageActivity
import com.jekro.lesjardindecaro.vibrateClickEffect
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import kotlin.random.Random

class DetailFragment : AbsFragment<DetailContract.View, DetailContract.Presenter>(),
DetailContract.View {

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
            priceLabelTextView.text = "Prix au Kilo :"
        }

        priceValueTextView.text = "${String.format("%.2f", product!!.price.fractional.toFloat() / 100)} €"
        if (!product.cat.isNullOrEmpty())
            categoryValueView.text = product.cat
        else
            categoryTextView.visibility = View.GONE

        product_number.setText(product.defaultQuantity.toString())

        var numberProduct = product_number.text.toString().toInt()
        minus?.setOnTouchListener { view, motionEvent ->
            animateButton(view, motionEvent) {
                if (numberProduct > product.defaultQuantity) {
                    numberProduct -= product.defaultQuantity
                    product_number.setText(numberProduct.toString())
                }
            }

            true
        }
        plus?.setOnTouchListener { view, motionEvent ->
            animateButton(view, motionEvent) {numberProduct += product.defaultQuantity
                product_number.setText(numberProduct.toString())}

            true
        }

        addBuy?.setOnClickListener {
            vibrateClickEffect()
            var cart = presenter.configurationRepo.getCart()
            if (cart == null)
                cart = Cart(Random(10000).nextInt(), "", mutableMapOf(), 0F, false, null)

            if (cart.productsQuantity[product] == null)
                cart.productsQuantity[product] =  Integer.parseInt(product_number.text.toString())
            else
                cart.productsQuantity[product] =  cart.productsQuantity[product]!!.plus(Integer.parseInt(product_number.text.toString()))

            cart.amountTotal = 0F
            cart.productsQuantity.keys.forEach { product ->
                val quantity = if (product.unity.isNullOrEmpty()) cart.productsQuantity[product]!! else cart.productsQuantity[product]!!/100
                val divider = if (product.unity.isNullOrEmpty()) 100 else 1000
                cart.amountTotal += ((product.price.fractional.toFloat() / divider) * quantity)
            }
            Snackbar.make(view!!, "Ce produit a été ajouté à votre panier", Snackbar.LENGTH_LONG).show()
            presenter.configurationRepo.saveCart(cart)
            activity?.onBackPressed()
        }
        if (shouldMoveCartButton != null && shouldMoveCartButton)
            (activity as HomePageActivity).moveCart()
    }

    private fun animateButton(view: View, motionEvent: MotionEvent, operation: (() -> Unit)?) {
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
            operation?.invoke()
        }
    }

    override fun displayResult() {
    }


    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    override fun getLayoutId(): Int = R.layout.fragment_detail

    override val presenter: DetailContract.Presenter by inject { parametersOf(this) }
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