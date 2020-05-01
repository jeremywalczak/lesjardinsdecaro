package com.jekro.lesjardindecaro

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jekro.lesjardindecaro.ui.cart.CartAdapter
import com.jekro.lesjardindecaro.ui.cart.CartContract

class SwipeToDeleteCallback(
    private val context: Context,
    private val adapter: CartAdapter,
    private val callback: CartContract.View
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val icon =
        ContextCompat.getDrawable(context, R.drawable.ic_trash)!!.mutate()
    private val background = ColorDrawable(ContextCompat.getColor(context, R.color.primary_01_500))
    private var swipeEnabled: Boolean = true

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder !is CartAdapter.ViewHolderProductInterface) 0 else super.getSwipeDirs(
            recyclerView,
            viewHolder
        )
    }

    override fun isItemViewSwipeEnabled(): Boolean = swipeEnabled

    fun setSwipeEnabled(enabled: Boolean) {
        swipeEnabled = enabled
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback.requestDeleteProduct(adapter.getProductAtPosition(viewHolder.adapterPosition))
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        recyclerView.isNestedScrollingEnabled = !isCurrentlyActive
        val itemView = viewHolder.itemView

        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        when {
            dX > 0 -> {
                val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
                val iconRight = itemView.left + iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom
                )
            }
            dX < 0 -> {
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            else ->
                background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.setTint(ContextCompat.getColor(context, R.color.basic_100))
        icon.draw(c)
    }
}