package com.id124.retrocoffee.activity.customer.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemHistoryProductsBinding
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat
import com.id124.retrocoffee.model.order.OrderModel

class HistoryAdapter(private val listOrder: ArrayList<OrderModel>, private val onListOrderClick:  onListOrderClickListener): RecyclerView.Adapter<HistoryAdapter.OrderHolder>() {

    fun addList(list: List<OrderModel>) {
        listOrder.clear()
        listOrder.addAll(list)
        notifyDataSetChanged()
    }

    class OrderHolder(val bind: ItemHistoryProductsBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_history_products, parent, false))
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val item = listOrder[position]

        holder.bind.price = currencyFormat(item.orTotal!!)
        holder.bind.date = item.orDate!!.split("T")[0]

        when (item.orStatus) {
            0 -> {
                holder.bind.status = "Waiting"
            }
            1 -> {
                holder.bind.status = "Proceed"
            }
            2 -> {
                holder.bind.status = "Reject"
            }
            3 -> {
                holder.bind.status = "Delivery"
            }
            4 -> {
                holder.bind.status = "Finish"
            }
        }

        holder.itemView.setOnClickListener{
            onListOrderClick.onOrderItem(position)
        }
    }

    override fun getItemCount(): Int = listOrder.size

    interface onListOrderClickListener  {
        fun onOrderItem(position : Int)
    }

}