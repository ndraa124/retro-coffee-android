package com.id124.retrocoffee.activity.customer.payment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemSliderBinding
import com.id124.retrocoffee.model.slider.SliderModel

class CardSliderAdapter(private val items: List<SliderModel>, viewPager2: ViewPager2) : RecyclerView.Adapter<CardSliderAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemSliderBinding

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(slider: SliderModel) {
            bind.ivCard.setImageResource(slider.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardSliderAdapter.RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_slider,
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
}