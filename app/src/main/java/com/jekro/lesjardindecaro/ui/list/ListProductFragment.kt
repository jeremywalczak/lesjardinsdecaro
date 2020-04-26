package com.jekro.lesjardindecaro.ui.list

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auchan.uikit.module.ModuleInteractor
import com.jekro.lesjardindecaro.mvp.AbsFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.addSearchDelayListener
import com.jekro.lesjardindecaro.hideKeyboard
import com.jekro.lesjardindecaro.model.AutoCompleteEntry
import com.jekro.lesjardindecaro.model.AutoCompleteViewType
import com.jekro.lesjardindecaro.model.Category
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.toDp
import com.jekro.lesjardindecaro.ui.DetailFragment
import com.jekro.lesjardindecaro.ui.home.HomePageActivity
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_autocomplete.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ListProductFragment : AbsFragment<ListProductContract.View, ListProductContract.Presenter>(),
    ListProductContract.View {

    override fun getLayoutId(): Int = R.layout.fragment_list

    override val presenter: ListProductContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()
    private var filtersChecked = arrayListOf<String>()
    private lateinit var iconSearchClose: Drawable
    private lateinit var iconSearch: Drawable
    private var isInSearchMode = false
    private var textWatcherQuery: TextWatcher? = null
    private var textWatcherDeleteSearch: TextWatcher? = null
    private var suggestionAdapter: AutoCompleteAdapter? = null
    private var productsAdapter: ListProductAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val products = arguments?.getParcelableArrayList<Product>(PRODUCTS)
        presenter.hashProductCategories = (arguments?.getSerializable(CATEGORIES_PRODUCTS)) as HashMap<Product, List<Category>>
        products?.let {
            presenter.buildFiltersType(it)
            presenter.initialEntries.addAll(it)
        }
        productsAdapter = ListProductAdapter(context!!, products?: arrayListOf())
        productsAdapter!!.onItemClick = {product ->
            product?.let {
                activity!!.supportFragmentManager.beginTransaction().add(
                    R.id.mainContainer,
                    DetailFragment.newInstance(it, false)
                ).addToBackStack(DetailFragment::class.java.toString()).commit()
            }
        }
        initAutoCompleteRecyclerView()
        initRecyclerView(productsAdapter!!)
        initFiltersChips()
        manageSearchEditText()
        (activity as HomePageActivity).moveCart()
    }

    override fun displayResult(products: List<Product>) {
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    override fun displaySuggestions(suggestions: List<AutoCompleteEntry>, search: String) {
        (activity as HomePageActivity).panierImageView.visibility = View.GONE
        autoCompleteRecycler.visibility =
            if (!suggestions.isNullOrEmpty() || search.isNullOrEmpty()) View.VISIBLE else View.GONE
        suggestionAdapter?.items = suggestions
        suggestionAdapter?.notifyDataSetChanged()
    }

    override fun displayProductsFiltered(products: List<Product>) {
        productsAdapter?.objects = ArrayList(products)
        productsAdapter?.notifyDataSetChanged()
        autocomplete_include.visibility = View.GONE
        search_edittext.clearFocus()
        hideKeyboard()
    }

    private fun initAutoCompleteRecyclerView() {
        suggestionAdapter = AutoCompleteAdapter(activity!!, arrayListOf())
        suggestionAdapter?.onItemClick = { autoCompleteEntry ->
            if (autoCompleteEntry.type == AutoCompleteViewType.ITEM) {
                when {
                    autoCompleteEntry.product != null -> {
                        getToProductDetail(null, autoCompleteEntry.product)
                    }
                    else -> {
                        displayProductsFiltered(presenter.getProductFiltered(null, autoCompleteEntry.product?.title))
                        initFiltersChips()
                    }
                }
            }
        }
        autoCompleteRecycler.adapter = suggestionAdapter
        autoCompleteRecycler.layoutManager = LinearLayoutManager(context)
        autoCompleteRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    activity?.hideKeyboard()
                }
            }
        })
    }

    private fun getToProductDetail(image: View?, item: Product) {
        hideKeyboard()
        val options = if (image != null) ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity!!,
            image,
            image.transitionName
        ) else null
            activity!!.supportFragmentManager.beginTransaction().add(
                R.id.mainContainer,
                DetailFragment.newInstance(product = item, shouldMoveCartButton = false)
            ).addToBackStack(DetailFragment::class.java.toString()).commit()
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

    private fun manageSearchEditText() {
        iconSearchClose =
            context!!.resources.getDrawable(R.drawable.delete_icon_item_coupons, activity!!.theme)
                .mutate()
        iconSearchClose.alpha = 0
        iconSearch =
            context!!.resources.getDrawable(R.drawable.search_icon_item_coupons, activity!!.theme)
                .mutate()
        search_edittext.setCompoundDrawablesWithIntrinsicBounds(
            iconSearch,
            null,
            iconSearchClose,
            null
        )
        search_cancel_button.visibility = if (isInSearchMode) View.VISIBLE else View.GONE

        if (textWatcherQuery == null) {
            textWatcherQuery = search_edittext.addSearchDelayListener(0) { search ->
                if (search_edittext?.hasFocus() == true) {
                    isInSearchMode = true
                    presenter.searchSuggestions(search)
                }
            }
        }

        if (textWatcherDeleteSearch == null) {
            textWatcherDeleteSearch = search_edittext.addSearchDelayListener(0) {
                if (it.isEmpty()) {
                    iconSearchClose.alpha = 0
                } else {
                    iconSearchClose.alpha = 255
                }
            }
        }

        search_edittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                displayProductsFiltered(
                    ArrayList(
                        presenter.getProductFiltered(
                            null,
                            search_edittext.text.toString()
                        )
                    )
                )
                initFiltersChips()
            }
            true
        }

        search_edittext.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        search_edittext.requestFocus()
                        if ((event.rawX + 64.toDp()) >= (search_edittext.right - search_edittext.compoundDrawables[2].bounds.width())) {
                            search_edittext.text.clear()
                        }
                        autocomplete_include.visibility = View.VISIBLE
                        search_cancel_button.visibility = View.VISIBLE
                        filters_chipgroup.visibility = View.GONE
                        filtersChecked.clear()
                        presenter.searchSuggestions(search_edittext.text.toString())
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        search_cancel_button.setOnTouchListener { view, motionEvent ->
            (activity as HomePageActivity).panierImageView.visibility = View.VISIBLE
            isInSearchMode = false
            filtersChecked.clear()
            search_edittext.setText("")
            /*couponAdapter?.objects =
                presenter.getCouponsSortedAndFiltered(null, null, null).toMutableList()
            couponAdapter?.notifyDataSetChanged()*/
            autocomplete_include.visibility = View.GONE
            search_cancel_button.visibility = View.GONE
            detail_toolbar.visibility = View.GONE
            search_edittext.clearFocus()
            initFiltersChips()
            hideKeyboard()
            true
        }
    }

    private fun initFiltersChips() {
        filters_chipgroup.visibility = if (presenter.filtersType?.isNullOrEmpty()) View.GONE else View.VISIBLE
        filters_chipgroup.removeAllViews()
        presenter.filtersType.forEach { term ->
            filters_chipgroup.addView(createChip(term))
        }
    }

    private fun createChip(term: String): Chip {
        return Chip(activity).apply {
            text = term
            isClickable = true
            isCheckable = true
            if (filtersChecked.contains(term)) {
                isChecked = true
                setTextColor(activity?.resources?.getColor(R.color.basic_600)!!)
                typeface = Typeface.DEFAULT_BOLD
            }
            setTextColor(activity?.resources?.getColor(R.color.basic_600)!!)
            setChipDrawable(
                ChipDrawable.createFromAttributes(
                    context!!,
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Filter
                )
            )
            isCheckedIconVisible = false
            chipStrokeWidth = 2F
            setEnsureMinTouchTargetSize(false)
            chipStrokeColor = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_selected),
                    intArrayOf(android.R.attr.state_selected)
                ),
                intArrayOf(
                    activity?.resources?.getColor(R.color.basic_400)!!,
                    activity?.resources?.getColor(R.color.primary_02_400)!!
                )
            )
            chipBackgroundColor = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_selected),
                    intArrayOf(android.R.attr.state_selected)
                ),
                intArrayOf(
                    activity?.resources?.getColor(R.color.basic_200)!!,
                    activity?.resources?.getColor(R.color.primary_02_400)!!
                )
            )
            setOnCheckedChangeListener { compoundButton, _ ->
                setTextColor(
                    if (compoundButton.isChecked) activity?.resources?.getColor(R.color.basic_200)!! else activity?.resources?.getColor(
                        R.color.basic_600
                    )!!
                )
                typeface = if (compoundButton.isChecked) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                filtersChecked = ArrayList(getChipsSelected())
                presenter.filter(filtersChecked, search_edittext.text.toString())
            }
            setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    animate().scaleX(0.925f).setDuration(100).scaleY(0.925f).start()
                } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                    animate().scaleX(1f).setDuration(100).scaleY(1f).start()
                }
                false
            }
        }
    }

    private fun getChipsSelected(): List<String> {
        val chipsSelected = mutableListOf<String>()
        for (i in 0..filters_chipgroup.childCount) {
            val chip = filters_chipgroup.getChildAt(i) as? Chip
            if (chip != null && chip.isChecked)
                chipsSelected.add(chip.text.toString())
        }
        return chipsSelected
    }

    companion object {
        const val PRODUCTS = "PRODUCTS"
        const val CATEGORIES_PRODUCTS = "CATEGORIES_PRODUCTS"
        fun newInstance(products: ArrayList<Product>?, hashProductCategories: HashMap<Product, List<Category>>) =
            ListProductFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PRODUCTS, products?: arrayListOf())
                    putSerializable(CATEGORIES_PRODUCTS, hashProductCategories)
                }
            }
    }

}