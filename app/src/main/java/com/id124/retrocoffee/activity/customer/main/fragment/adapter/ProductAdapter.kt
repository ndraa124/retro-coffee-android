package com.id124.retrocoffee.activity.customer.main.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemProductsBinding
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemProductsBinding
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var items = mutableListOf<ProductModel>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pr: ProductModel) {
            bind.product = pr

            if (pr.prPicImage != null) {
                bind.imageUrl = BASE_URL_IMAGE + pr.prPicImage
            }

            bind.price = currencyFormat(pr.prPrice.toString())
            bind.executePendingBindings()

            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(pr)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_products,
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

    interface OnItemClickCallback {
        fun onItemClick(data: ProductModel)
    }

    fun addList(list: List<ProductModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}