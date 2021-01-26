package com.id124.retrocoffee.activity.customer.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.product_detail.ProductDetailActivity
import com.id124.retrocoffee.databinding.ItemSearchProductBinding
import com.id124.retrocoffee.model.favorite.FavoriteModel
import java.text.NumberFormat
import java.util.*

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {
    private var items = mutableListOf<FavoriteModel>()

    fun addList(list: List<FavoriteModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class FavoriteHolder(val binding: ItemSearchProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        return FavoriteHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search_product, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        val item = items[position]
        val format = NumberFormat.getCurrencyInstance()
        val context = holder.binding.itemProduct.context
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IDR")

        holder.binding.tvProductName.text = item.prName
        holder.binding.tvProductPrice.text = format.format(item.prPrice.toDouble())

        Glide.with(context)
            .load("http://18.212.11.190:3000/images/${item.prPic}")
            .into(holder.binding.ivProductImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("productID", item.prID)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}