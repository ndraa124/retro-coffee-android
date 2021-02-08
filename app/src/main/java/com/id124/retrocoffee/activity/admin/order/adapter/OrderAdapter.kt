package com.id124.retrocoffee.activity.admin.order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemOrderBinding
import com.id124.retrocoffee.model.order_admin.OrderModel
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat
import com.id124.retrocoffee.util.Utils.Companion.formatDate1
import com.id124.retrocoffee.util.Utils.Companion.formatDate2

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemOrderBinding
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var items = mutableListOf<OrderModel>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(or: OrderModel) {
            val orderId = or.orId
            val orderDate = or.orDateOrder.split("T")[0]
            val noTransaction = formatDate1(orderDate)

            bind.orderId = "RCS-${orderId}${noTransaction}"
            bind.payTotal = "${or.historyProduct.size} item - ${currencyFormat(or.orPayTotal)}"

            when (or.orStatus) {
                0 -> {
                    bind.clOrder.backgroundColor = itemView.context.getColor(R.color.secondary)
                    bind.tvStatus.setTextColor(itemView.context.getColor(R.color.secondary))
                    bind.status = "Waiting"
                }
                1 -> {
                    bind.clOrder.backgroundColor = itemView.context.getColor(R.color.design_default_color_primary_variant)
                    bind.tvStatus.setTextColor(itemView.context.getColor(R.color.design_default_color_primary_variant))
                    bind.status = "Proceed"
                }
                2 -> {
                    bind.clOrder.backgroundColor = itemView.context.getColor(R.color.design_default_color_error)
                    bind.tvStatus.setTextColor(itemView.context.getColor(R.color.design_default_color_error))
                    bind.status = "Reject"
                }
                3 -> {
                    bind.clOrder.backgroundColor = itemView.context.getColor(R.color.design_default_color_secondary_variant)
                    bind.tvStatus.setTextColor(itemView.context.getColor(R.color.design_default_color_secondary_variant))
                    bind.status = "Delivery"
                }
                4 -> {
                    bind.clOrder.backgroundColor = itemView.context.getColor(R.color.green)
                    bind.tvStatus.setTextColor(itemView.context.getColor(R.color.green))
                    bind.status = "Done"
                }
            }

            bind.date = formatDate2(or.orDateOrder.split("T")[0])

            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(or)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_order,
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
        fun onItemClick(data: OrderModel)
    }

    fun addList(list: List<OrderModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}