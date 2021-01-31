package com.id124.retrocoffee.activity.customer.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemHistoryBinding
import com.id124.retrocoffee.model.history.HistoryModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemHistoryBinding
    private var items = mutableListOf<HistoryModel>()

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hr: HistoryModel) {
            if (hr.htImage != null) {
                bind.imageUrl = BASE_URL_IMAGE + hr.htImage
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_history,
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