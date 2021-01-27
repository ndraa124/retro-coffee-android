package com.id124.retrocoffee.activity.customer.history.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemHistoryProductsBinding
import com.id124.retrocoffee.model.history.HistoryModel

class HistoryAdapter(private val listHistory: ArrayList<HistoryModel>, private val onListHistoryClick:  onListHistoryClickListener): RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    fun addList(list: List<HistoryModel>) {
        listHistory.clear()
        listHistory.addAll(list)
        notifyDataSetChanged()
    }

    class HistoryHolder(val bind: ItemHistoryProductsBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        return HistoryHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_history_products, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        val item = listHistory[position]

        holder.bind.tvTitle.text =item.htProduct


        holder.itemView.setOnClickListener{
            onListHistoryClick.onHistoryItem(position)
        }
    }

    override fun getItemCount(): Int = listHistory.size

    interface onListHistoryClickListener  {
        fun onHistoryItem(position : Int)
    }

}