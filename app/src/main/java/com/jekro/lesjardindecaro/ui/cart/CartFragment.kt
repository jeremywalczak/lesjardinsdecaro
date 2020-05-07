package com.jekro.lesjardindecaro.ui.cart

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jekro.lesjardindecaro.*
import com.jekro.lesjardindecaro.module.ModuleInteractor
import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.mvp.AbsFragment
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.include_empty.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CartFragment : AbsFragment<CartContract.View, CartContract.Presenter>(),
    CartContract.View {

    override fun getLayoutId(): Int = R.layout.fragment_cart

    private var adapter: CartAdapter? = null
    private var handler = Handler()
    private var swipeToDelete: SwipeToDeleteCallback? = null
    private var buttonWrapContent: Boolean? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var deleteDisplayedOnce = false

    override val presenter: CartContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun displayResult(cart: Cart) {
        if (adapter != null) {
            adapter!!.items = cart.productsQuantity.keys.toMutableList()
            adapter!!.notifyDataSetChanged()
        } else {
            adapter = CartAdapter(context!!, cart.productsQuantity.keys.toMutableList(), cart)
            products_list_recycler.adapter = adapter
        }

        initAdapterCallback()
        if (!deleteDisplayedOnce) {
            deleteDisplayedOnce = true
            displayFirstProductAnimation(0)
        }
        initSwipe()
        initPriceButton(cart)
        empty_container.visibility = if (presenter.cart == null || presenter.cart?.productsQuantity.isNullOrEmpty()) View.VISIBLE else View.GONE
        main_container.visibility = if (presenter.cart == null || presenter.cart?.productsQuantity.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun displayFirstProductAnimation(indexFirstProduct: Int) {
        handler.post {
            val firstProduct = (products_list_recycler.layoutManager as LinearLayoutManager).getChildAt(indexFirstProduct)!!
            cart_trash_demo.y = firstProduct.top.toFloat()
            firstProduct.animate().x(-80.toPx().toFloat()).setDuration(300).withEndAction {
                handler.postDelayed({
                    firstProduct.animate().x(0.toFloat())
                }, 400)
            }
        }
    }

    override fun requestDeleteProduct(product: Product) {
        presenter.deleteProduct(product.id!!)
        swipeToDelete?.setSwipeEnabled(false)
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        presenter.cart = presenter.configurationRepo.getCart()
        if (presenter.cart != null && !presenter.cart?.productsQuantity.isNullOrEmpty()) {
            displayResult(presenter.cart!!)
        }
        empty_container.visibility = if (presenter.cart == null || presenter.cart?.productsQuantity.isNullOrEmpty()) View.VISIBLE else View.GONE
        main_container.visibility = if (presenter.cart == null || presenter.cart?.productsQuantity.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (presenter.configurationRepo.getCart() == null) {
            empty_container.visibility = View.VISIBLE
            main_container.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context!!)
        products_list_recycler.layoutManager = linearLayoutManager
    }

    private fun initPriceButton(cart: Cart) {
        cart_validate_button.text = "Valider mon panier : " + String.format("%.2f",cart.amountTotal) + " €"

        cart_validate_button.setOnClickListener {
            vibrateClickEffect()
           startActivity(Intent(context, ValidateCartActivity::class.java), null)
        }
    }


    private fun initAdapterCallback() {
        adapter!!.cartCallback = object : CartAdapter.CartCallback {
            override fun onEmptyCartContinueClicked() {

            }

            override fun onQuantityAddRequested(value: Int, id: String) {
                vibrateClickEffect()
                presenter.updateQuantity(id, value)
            }

            override fun onQuantityRemoveRequested(value: Int, id: String) {
                vibrateClickEffect()
                presenter.updateQuantity(id, value)
            }

            override fun onCartClearClicked() {
                /*activity?.showDialogWithConfirm(
                    title = getString(R.string.mcommerce_clear_confirmation),
                    okButton = getString(R.string.mcommerce_clear_cart),
                    oKFunction = {
                        trackEvent("Click - Empty basket", "", "", "Mcommerce - Basket")
                        presenter.clearCart()
                    },
                    cancelButton = getString(R.string.common_cancel),
                    cancellable = true
                )*/
            }

            override fun onCartValidateClicked() {
                //presenter.validateCart()
            }
        }
    }

    override fun displayPopUpDelete(id: String, quantity: Int) {
        activity?.showDialogWithConfirm(
            title = "Suppression produit",
            message = "Etes-vous sûr de vouloir supprimer ce produit ?",
            okButton = "Supprimer",
            oKFunction = { presenter.forceUpdateQuantity(id, quantity) },
            cancelButton = "Annuler",
            cancelFunction = {
                swipeToDelete?.setSwipeEnabled(true)
                adapter?.notifyDataSetChanged()
            },
            cancellable = false
        )
    }

    private fun initSwipe() {
        swipeToDelete = SwipeToDeleteCallback(context!!, adapter!!, this)
        val itemTouchHelper = ItemTouchHelper(swipeToDelete!!)
        itemTouchHelper.attachToRecyclerView(products_list_recycler)
        swipeToDelete!!.setSwipeEnabled(true)
    }

}