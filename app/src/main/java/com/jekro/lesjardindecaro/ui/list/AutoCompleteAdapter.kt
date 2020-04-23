package com.jekro.lesjardindecaro.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.AutoCompleteEntry
import com.jekro.lesjardindecaro.model.AutoCompleteViewType
import com.jekro.lesjardindecaro.setHtmlText
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_autocomplete_header.*
import kotlinx.android.synthetic.main.list_item_autocomplete_item.*


class AutoCompleteAdapter(
    private val context: Context,
    var items: List<AutoCompleteEntry>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onItemClick: ((AutoCompleteEntry) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            AutoCompleteViewType.HEADER.ordinal -> HeaderViewHolder(
                inflater.inflate(
                    R.layout.list_item_autocomplete_header,
                    parent,
                    false
                )
            )
            AutoCompleteViewType.ITEM.ordinal -> ItemViewHolder(
                inflater.inflate(
                    R.layout.list_item_autocomplete_item,
                    parent,
                    false
                )
            )
            else -> throw Exception("should not happen")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            AutoCompleteViewType.HEADER.ordinal -> {
                (holder as HeaderViewHolder).list_item_header_value.setHtmlText(items[position].displayValue)
            }
            AutoCompleteViewType.ITEM.ordinal -> {
                (holder as ItemViewHolder).list_item_item_value.setHtmlText(items[position].displayValue)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = items[position].type.ordinal

    inner class ItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            itemView.setOnClickListener {
                onItemClick.invoke(items[adapterPosition])
            }
        }
    }

    inner class HeaderViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer
}

