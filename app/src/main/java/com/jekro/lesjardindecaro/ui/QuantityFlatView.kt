package com.jekro.lesjardindecaro.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.jekro.lesjardindecaro.R
import kotlinx.android.synthetic.main.view_quantity.view.*

class QuantityFlatView : LinearLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private var max: Int? = null
    private var value: Int = 0
    private var valueChangeListener: ValueChangeListener? = null
    private var small: Boolean = false
    private var asyncListener: AsyncChangeRequestListener? = null

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.QuantityFlatView)
            if (typedArray.hasValue(R.styleable.QuantityFlatView_max)) {
                max = typedArray.getInteger(R.styleable.QuantityFlatView_max, Integer.MAX_VALUE)
            }
            if (typedArray.hasValue(R.styleable.QuantityFlatView_value)) {
                quantity_value.text =
                    "${typedArray.getInteger(R.styleable.QuantityFlatView_value, value)}"
            }
            if(typedArray.hasValue(R.styleable.QuantityFlatView_small)){
                small= typedArray.getBoolean(R.styleable.QuantityFlatView_small, false)
            }
            typedArray.recycle()
        }

        View.inflate(context, if (small) R.layout.view_quantity_small else R.layout.view_quantity, this)

        quantity_remove.setOnClickListener {
            if (asyncListener != null) {
                if (value > 0) {
                    quantity_loading.visibility = View.VISIBLE
                    quantity_value.visibility = View.GONE
                    quantity_remove.isEnabled = false
                    quantity_add.isEnabled = false
                    asyncListener!!.onQuantityRemoveRequested(value - 1)
                }
            } else {
                if (value > 0) {
                    setValue(value - 1)
                }
            }
        }

        quantity_add.setOnClickListener {
            if (asyncListener != null) {
                if (max == null || (value < max!!)) {
                    quantity_loading.visibility = View.VISIBLE
                    quantity_value.visibility = View.GONE
                    quantity_remove.isEnabled = false
                    quantity_add.isEnabled = false
                    asyncListener!!.onQuantityAddRequested(value + 1)
                }
            } else {
                if (max == null || (value < max!!)) {
                    setValue(value + 1)
                }
            }
        }
    }

    fun setValueChangerListener(listener: ValueChangeListener) {
        valueChangeListener = listener
    }

    fun setAsyncChangeRequestListener(listener: AsyncChangeRequestListener) {
        asyncListener = listener
    }

    fun getValue(): Int = value

    fun setValue(newValue: Int) {
        quantity_loading.visibility = View.GONE
        quantity_value.visibility = View.VISIBLE
        value = newValue
        quantity_value.text = newValue.toString()
        valueChangeListener?.onQuantityChanged(newValue)
        enableButtons()
    }

    fun setMax(max: Int?) {
        this.max = max
        enableButtons()
    }

    private fun enableButtons() {
        quantity_remove.isEnabled = value > 0
        if (max == null || value < max!!) {
            quantity_add.isEnabled = true
            quantity_add.setImageResource(R.drawable.ic_plus)
        } else {
            quantity_add.isEnabled = false
            quantity_add.setImageResource(R.drawable.ic_max)
        }
        if (value > 0) {
            quantity_remove.isEnabled = true
        }
    }

    interface ValueChangeListener {

        fun onQuantityChanged(value: Int)
    }

    interface AsyncChangeRequestListener {

        fun onQuantityRemoveRequested(newValue: Int)

        fun onQuantityAddRequested(newValue: Int)

    }
}