package com.id124.retrocoffee.activity.admin.product.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemProductAdminBinding
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemProductAdminBinding
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

            if (pr.prIsDiscount == 1) {
                bind.tvPrice.paintFlags = bind.tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                bind.price = currencyFormat(pr.prPrice.toString())
                bind.pricePromo = currencyFormat(pr.prDiscountPrice.toString())
            } else {
                bind.price = currencyFormat(pr.prPrice.toString())

                bind.tvPricePromo.visibility = View.GONE
                bind.pricePromo = currencyFormat(pr.prDiscountPrice.toString())
            }
            
            bind.executePendingBindings()

            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(pr)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_product_admin,
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