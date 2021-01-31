package com.id124.retrocoffee.activity.customer.favorite.favorite_detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.favorite.FavoritePresenter
import com.id124.retrocoffee.activity.customer.login.LoginViewModel
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityFavoriteDetailBinding
import kotlinx.coroutines.CoroutineScope

class FavoriteDetailActivity : BaseActivity<ActivityFavoriteDetailBinding>() {

    private lateinit var viewModel:FavoriteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_favorite_detail
        super.onCreate(savedInstanceState)

        // Set View Model
        setViewModel()

        // Set Intent Data
        setData()

        // Observe Live Data
        subscribeLiveData()

        // Delete Action
        deleteFavorite()

    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this, {
            bind.loadingScreen.visibility = View.VISIBLE
            bind.progressBar.max = 100
        })

        viewModel.onSuccessLiveData.observe(this, {
            if (it) {
                bind.loadingScreen.visibility = View.GONE
                Toast.makeText(this, "Favorite Deleted Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        viewModel.onFailLiveData.observe(this, {
            noticeToast(it)
        })
    }

    private fun deleteFavorite() {
        bind.btDeleteFavorite.setOnClickListener {
            viewModel.deleteFavorite(intent.getIntExtra("favoriteID", 0))
        }
    }

    private fun setData() {
        bind.tvProductName.text = intent.getStringExtra("productName")
        bind.tvProductDesc.text = intent.getStringExtra("productDesc")
        Glide.with(this)
            .load("http://18.212.11.190:3000/images/${intent.getStringExtra("productImage")}")
            .into(bind.ivProductImage)

    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this).get(FavoriteDetailViewModel::class.java)
        viewModel.setService(createApi(this))
    }

}