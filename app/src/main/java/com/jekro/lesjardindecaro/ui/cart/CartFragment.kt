package com.jekro.lesjardindecaro.ui.cart

import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auchan.uikit.module.ModuleInteractor
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.SwipeToDeleteCallback
import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.mvp.AbsFragment
import com.jekro.lesjardindecaro.toPx
import com.jekro.lesjardindecaro.vibrateClickEffect
import kotlinx.android.synthetic.main.fragment_cart.*
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
            adapter!!.clearLoadingAndNotifyDataSetChanged()
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
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        if (adapter == null) {
            presenter.cart = presenter.configurationRepo.getCart()
            presenter.cart?.let {
                displayResult(it)
            }
        } else {
            products_list_recycler.adapter = adapter
            initPriceButton(adapter!!.cart)
            initSwipe()
            handler.post {
                presenter.updateViewButtons()
            }
        }
    }

    private fun initRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context!!)
        products_list_recycler.layoutManager = linearLayoutManager
        products_list_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                updateButtonPosition()
            }
        })
    }

    private fun updateButtonPosition() {
        /*if (presenter.isCartEmpty())
            return

        handler.post {
            val position = linearLayoutManager?.findLastVisibleItemPosition()

            if (position == -1 || position == null)
                return@post

            if (adapter != null && adapter?.isFooter(position) == true) {
                val view = linearLayoutManager!!.findViewByPosition(position)
                val parentTop = view!!.top
                val buttonTop = view.findViewById<View>(getViewToAnimateInRecycler())
                val totalTop = parentTop + buttonTop.top
                if (totalTop > getViewToAnimateInRoot().top) {
                    if (buttonWrapContent == null || !buttonWrapContent!!) {
                        expandBottomButton()
                    }
                } else {
                    if (buttonWrapContent == null || buttonWrapContent!!) {
                        collapseBottomButton()
                    }
                }
                getViewToAnimateInRoot().y = min(totalTop, getViewToAnimateInRoot().top).toFloat()
            }
        }*/
    }

    private fun initPriceButton(cart: Cart) {
        cart_validate_button.text = "Total : " + cart.amountTotal

        cart_validate_button.setOnClickListener {
           /* trackEvent("Click - Validate basket", "", "", "Mcommerce - Basket")
            cart_validate_button.isEnabled = false
            presenter.validateCart()*/
        }
    }


    private fun initAdapterCallback() {
        adapter!!.cartCallback = object : CartAdapter.CartCallback {
            override fun onEmptyCartContinueClicked() {

            }

            override fun onQuantityAddRequested(value: Int, id: String) {
                vibrateClickEffect()
                presenter.updateQuantity(value, id)
            }

            override fun onQuantityRemoveRequested(value: Int, id: String) {
                vibrateClickEffect()
                presenter.updateQuantity(value, id)
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

    private fun initSwipe() {
        swipeToDelete = SwipeToDeleteCallback(context!!, adapter!!, this)
        val itemTouchHelper = ItemTouchHelper(swipeToDelete!!)
        itemTouchHelper.attachToRecyclerView(products_list_recycler)
        swipeToDelete!!.setSwipeEnabled(true)
    }

}