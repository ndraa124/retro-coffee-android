package com.id124.retrocoffee.activity.admin.order.detail_order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemOrderDetailBinding
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemOrderDetailBinding
    private var items = mutableListOf<HistoryModel>()

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(ht: HistoryModel) {
            bind.history = ht

            if (ht.htImage != null) {
                bind.imageUrl = BASE_URL_IMAGE + ht.htImage
            }

            bind.price = currencyFormat(ht.htPrice.toString())
            bind.qty = "x ${ht.htQty}"

            bind.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_order_detail,
            parent,
            false
        )
        return RecyclerViewHolder(bind.root)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addList(list: List<HistoryModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}