package com.id124.retrocoffee.activity.customer.history_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemHistoryDetailProductsBinding
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.Utils

class HistoryDetailAdapter(private val listHistory: ArrayList<HistoryModel>): RecyclerView.Adapter<HistoryDetailAdapter.HistoryHolder>(){
    
    fun addList(list: List<HistoryModel>) {
        listHistory.clear()
        listHistory.addAll(list)
        notifyDataSetChanged()
    }

    class HistoryHolder(val bind: ItemHistoryDetailProductsBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        return HistoryHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_history_detail_products, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        val item = listHistory[position]


        if (item.htImage != null) {
            holder.bind.imageUrl = BASE_URL_IMAGE + item.htImage
        }

        holder.bind.price = Utils.currencyFormat(item.htPrice.toString())
        holder.bind.productName = item.htProduct.toString()
        holder.bind.qty = item.htQty.toString()
    }

    override fun getItemCount(): Int  = listHistory.size

}