package com.id124.retrocoffee.activity.customer.payment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemCartCheckoutBinding
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat

class CartAdapter : RecyclerView.Adapter<CartAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemCartCheckoutBinding
    private var items = mutableListOf<CartModel>()

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cr: CartModel) {
            bind.cart = cr

            if (cr.crPicImage != null) {
                bind.imageUrl = BASE_URL_IMAGE + cr.crPicImage
            }

            bind.price = currencyFormat(cr.crPrice.toString())
            bind.qty = "x ${cr.crQty}"

            bind.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_cart_checkout,
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

    fun addList(list: List<CartModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}