package com.id124.retrocoffee.activity.customer.cart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemCartBinding
import com.id124.retrocoffee.model.cart.CartModel
import com.id124.retrocoffee.remote.ApiClient
import com.id124.retrocoffee.util.Utils

class AdapterCart(private val items : ArrayList<CartModel>, private val onRecyclerViewClickListener: OnRecyclerViewClickListener) : RecyclerView.Adapter<AdapterCart.RecyclerViewHolder>() {
    private lateinit var bind: ItemCartBinding

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cr: CartModel) {
            bind.cart = cr


            if (cr.crPicImage != null) {
                bind.imageUrl = ApiClient.BASE_URL_IMAGE + cr.crPicImage
            }

            bind.price = Utils.currencyFormat(cr.crPrice.toString())
            bind.qty = "x ${cr.crQty}"

            bind.executePendingBindings()


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

        bind.icDelete.setOnClickListener {
            onRecyclerViewClickListener.onRecyclerViewItemClicked(position)
        }
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