package com.jekro.lesjardindecaro.ui.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auchan.uikit.module.ModuleInteractor
import com.auchan.uikit.mvp.AbsFragment
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.hideKeyboard
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.ui.home.HomePageFragment.Companion.PRODUCTS
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ListProductFragment : AbsFragment<ListProductContract.View, ListProductContract.Presenter>(),
    ListProductContract.View {
    override fun displayResult(products: List<Product>) {
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    override fun getLayoutId(): Int = R.layout.fragment_list

    override val presenter: ListProductContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val products = activity?.intent?.getParcelableArrayListExtra<Product>(PRODUCTS)
        val productsAdapter = ListProductAdapter(context!!, products!!)
        initRecyclerView(productsAdapter)
    }


    private fun initRecyclerView(productAdapter: ListProductAdapter) {
        search_linearlayout.post {
            main_container.visibility = View.VISIBLE
            main_container?.let {
                main_container.layoutManager =
                    LinearLayoutManager(view!!.context, RecyclerView.VERTICAL, false)
                main_container.setPadding(
                    0,
                    if (filters_chipgroup.visibility == View.VISIBLE) (search_linearlayout.height * 1.1).toInt() else search_linearlayout.height,
                    0,
                    0
                )
                main_container.scrollToPosition(0)
                search_linearlayout.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
                    main_container.setPadding(
                        0,
                        if (filters_chipgroup.visibility == View.VISIBLE) (search_linearlayout.height * 1.1).toInt() else search_linearlayout.height,
                        0,
                        0
                    )
                    main_container.scrollToPosition(0)
                }
                detail_toolbar.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
                    main_container.setPadding(0, (detail_toolbar.height * 1.1).toInt(), 0, 0)
                    main_container.scrollToPosition(0)
                }
                main_container.adapter = productAdapter
                main_container.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                            activity?.hideKeyboard()
                        }
                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy >= 0) {
                            if (search_linearlayout.y - dy.toFloat() > -search_linearlayout.bottom)
                                search_linearlayout.y -= dy.toFloat()
                        } else {
                            if (search_linearlayout.y < 0F)
                                search_linearlayout.y -= dy.toFloat()
                            else
                                search_linearlayout.y = 0F
                        }
                    }
                })
            }
        }
    }

}