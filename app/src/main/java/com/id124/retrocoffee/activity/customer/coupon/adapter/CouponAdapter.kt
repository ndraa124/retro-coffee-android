package com.id124.retrocoffee.activity.customer.coupon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemCouponsBinding
import com.id124.retrocoffee.model.coupon.CouponModel

class CouponAdapter : RecyclerView.Adapter<CouponAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemCouponsBinding
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var items = mutableListOf<CouponModel>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cp: CouponModel) {
            bind.coupon = cp
            bind.clCoupon.backgroundColor = itemView.context.getColor(cp.cpColor)
            bind.ivImage.setImageResource(cp.cpPicImage)
            bind.executePendingBindings()

            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(cp)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_coupons,
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
        fun onItemClick(data: CouponModel)
    }

    fun addList(list: List<CouponModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}