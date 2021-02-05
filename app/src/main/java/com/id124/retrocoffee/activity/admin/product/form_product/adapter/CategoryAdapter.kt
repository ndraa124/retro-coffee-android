package com.id124.retrocoffee.activity.admin.product.form_product.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.id124.retrocoffee.R
import com.id124.retrocoffee.databinding.ItemCategorySpinnerBinding
import com.id124.retrocoffee.model.category.CategoryModel

class CategoryAdapter(private var context: Context) : BaseAdapter() {
    private lateinit var bind: ItemCategorySpinnerBinding
    private var items = mutableListOf<CategoryModel>()

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, parent: ViewGroup): View {
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            bind = DataBindingUtil.inflate(inflater, R.layout.item_category_spinner, parent, false)
        }

        bind.category = items[i]

        return bind.root
    }

    fun addList(list: List<CategoryModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}