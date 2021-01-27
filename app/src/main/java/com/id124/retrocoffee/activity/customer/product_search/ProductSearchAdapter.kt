package com.id124.retrocoffee.activity.customer.product_search

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
import com.id124.retrocoffee.model.product.ProductModel
import java.text.NumberFormat
import java.util.*

class ProductSearchAdapter: RecyclerView.Adapter<ProductSearchAdapter.ProductHolder>() {
    private var items = mutableListOf<ProductModel>()

    fun addList(list: List<ProductModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ProductHolder(val binding: ItemSearchProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search_product, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val item = items[position]
        val format = NumberFormat.getCurrencyInstance()
        val context = holder.binding.itemProduct.context
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IDR")

        holder.binding.tvProductName.text = item.prName
        holder.binding.tvProductPrice.text = format.format(item.prPrice.toDouble())

        Glide.with(context)
            .load("http://18.212.11.190:3000/images/${item.prPicImage}")
            .into(holder.binding.ivProductImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("ct_id", item.ctId)
            intent.putExtra("ct_name", item.ctName)
            intent.putExtra("pr_id", item.prId)
            intent.putExtra("pr_status", item.prStatus)
            intent.putExtra("pr_name", item.prName)
            intent.putExtra("pr_price", item.prPrice)
            intent.putExtra("pr_pic_image", item.prPicImage)
            intent.putExtra("pr_desc", item.prDesc)
            intent.putExtra("pr_discount", item.prDiscount)
            intent.putExtra("pr_discount_price", item.prDiscountPrice)
            intent.putExtra("pr_is_discount", item.prDiscount)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}