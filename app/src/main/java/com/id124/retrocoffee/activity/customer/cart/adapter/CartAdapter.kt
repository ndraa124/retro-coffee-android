package com.id124.retrocoffee.activity.customer.cart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemCartBinding
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat

class CartAdapter : RecyclerView.Adapter<CartAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemCartBinding
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var items = mutableListOf<CartModel>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cr: CartModel) {
            bind.cart = cr

            if (cr.crPicImage != null) {
                bind.imageUrl = BASE_URL_IMAGE + cr.crPicImage
            }

            bind.price = currencyFormat(cr.crPrice.toString())
            bind.qty = "x ${cr.crQty}"

            bind.executePendingBindings()

            bind.ivAdd.setOnClickListener {
                onItemClickCallback.onClickAddItem(cr)
            }

            bind.ivRemove.setOnClickListener {
                onItemClickCallback.onClickRemoveItem(cr)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_cart,
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
        fun onClickAddItem(data: CartModel)
        fun onClickRemoveItem(data: CartModel)
    }

    fun addList(list: List<CartModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}