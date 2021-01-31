package com.id124.retrocoffee.activity.customer.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.navigation.NavigationView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.CartActivity
import com.id124.retrocoffee.activity.customer.favorite.FavoriteActivity
import com.id124.retrocoffee.activity.customer.history.HistoryActivity
import com.id124.retrocoffee.activity.customer.main.fragment.ProductFragment
import com.id124.retrocoffee.activity.customer.main.fragment.adapter.PromoteAdapter
import com.id124.retrocoffee.activity.customer.product_detail.ProductDetailActivity
import com.id124.retrocoffee.activity.customer.product_search.ProductSearchActivity
import com.id124.retrocoffee.activity.customer.profile.ProfileActivity
import com.id124.retrocoffee.activity.customer.promote.PromoteActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityMainBinding
import com.id124.retrocoffee.model.product.ProductModel
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.ViewPagerAdapter
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: PromoteAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_main
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setNavigationDrawer()
        setNavigationDrawerHeader()
        setProductRecyclerView()
        setViewModel()
        subscribeCategoryLiveData()
        subscribeProductLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ln_search -> {
                intents<ProductSearchActivity>(this@MainActivity)
            }
            R.id.tv_more_product -> {
                intents<ProductSearchActivity>(this@MainActivity)
            }
            R.id.tv_more_promo -> {
                intents<PromoteActivity>(this@MainActivity)
            }
            R.id.btn_logout -> {
                bind.drawerLayout.closeDrawer(GravityCompat.START)
                signOutConfirm()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                intents<ProfileActivity>(this@MainActivity)
            }
            R.id.nav_orders -> {
                intents<HistoryActivity>(this@MainActivity)
            }
            R.id.nav_all_menu -> {
                intents<FavoriteActivity>(this@MainActivity)
            }
            R.id.nav_promo -> {
                intents<PromoteActivity>(this@MainActivity)
            }
        }

        bind.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (bind.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            bind.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cart -> {
                intents<CartActivity>(this@MainActivity)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        setNavigationDrawerHeader()
    }

    override fun onResume() {
        super.onResume()
        setNavigationDrawerHeader()
    }

    private fun setToolbarActionBar() {
        setStatusBar()
        setSupportActionBar(bind.toolbar)
        supportActionBar?.title = ""
    }

    private fun setStatusBar() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = this.resources.getColor(R.color.background, theme)
    }

    private fun setNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
            this@MainActivity,
            bind.drawerLayout,
            bind.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_humburger)
        toggle.setToolbarNavigationClickListener {
            bind.drawerLayout.openDrawer(GravityCompat.START)
        }

        bind.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        bind.navView.setNavigationItemSelectedListener(this@MainActivity)
    }

    private fun setNavigationDrawerHeader() {
        val navHead: View = bind.navView.getHeaderView(0)
        val ivProfile = navHead.findViewById<CircleImageView>(R.id.iv_profile)
        val tvName = navHead.findViewById<TextView>(R.id.tv_name)
        val tvEmail = navHead.findViewById<TextView>(R.id.tv_email)

        if (sharedPref.getCsPicImage() == null || sharedPref.getCsPicImage() == "") {
            ivProfile.setImageResource(R.drawable.profile)
        } else {
            Glide.with(this@MainActivity)
                .asBitmap()
                .load(BASE_URL_IMAGE + sharedPref.getCsPicImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        ivProfile.setImageBitmap(resource)
                        ivProfile.buildLayer()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onLoadStarted(placeholder: Drawable?) {
                        ivProfile.setImageResource(R.drawable.ic_loading)
                    }
                })
        }

        tvName.text = sharedPref.getAcName()
        tvEmail.text = sharedPref.getAcEmail()
    }

    private fun setProductRecyclerView() {
        bind.rvProduct.isNestedScrollingEnabled = true

        layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        bind.rvProduct.layoutManager = layoutManager

        adapter = PromoteAdapter()
        bind.rvProduct.adapter = adapter

        adapter.setOnItemClickCallback(object : PromoteAdapter.OnItemClickCallback {
            override fun onItemClick(data: ProductModel) {
                val intent = Intent(this@MainActivity, ProductDetailActivity::class.java)
                intent.putExtra("ct_id", data.ctId)
                intent.putExtra("ct_name", data.ctName)
                intent.putExtra("pr_id", data.prId)
                intent.putExtra("pr_name", data.prName)
                intent.putExtra("pr_price", data.prPrice)
                intent.putExtra("pr_desc", data.prDesc)
                intent.putExtra("pr_discount", data.prDiscount)
                intent.putExtra("pr_discount_price", data.prDiscountPrice)
                intent.putExtra("pr_is_discount", data.prIsDiscount)
                intent.putExtra("pr_status", data.prStatus)
                intent.putExtra("pr_pic_image", data.prPicImage)
                startActivity(intent)
            }
        })
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
        viewModel.setServiceCategory(createApi(this@MainActivity))
        viewModel.setServiceProduct(createApi(this@MainActivity))
        viewModel.serviceGetCategoryApi()
        viewModel.serviceGetPromoApi()
    }

    private fun subscribeCategoryLiveData() {
        viewModel.isLoadingCategory.observe(this@MainActivity) {
            if (it) {
                bind.progressBarCategory.visibility = View.VISIBLE
            } else {
                bind.progressBarCategory.visibility = View.GONE
            }
        }

        viewModel.onSuccessCategory.observe(this@MainActivity) { list ->
            bind.tvDataNotFoundCategory.visibility = View.GONE

            bind.tabLayoutProduct.setupWithViewPager(bind.viewPagerProduct)
            val adapter = ViewPagerAdapter(supportFragmentManager)

            list.map {
                adapter.addFrag(ProductFragment(it.ctId), it.ctName)
            }

            bind.viewPagerProduct.adapter = adapter
        }

        viewModel.onFailCategory.observe(this@MainActivity) { message ->
            bind.dataNotFoundCategory = message
            bind.tvDataNotFoundCategory.visibility = View.VISIBLE
        }
    }

    private fun subscribeProductLiveData() {
        viewModel.isLoadingProduct.observe(this@MainActivity) {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccessProduct.observe(this@MainActivity) { list ->
            bind.tvDataNotFound.visibility = View.GONE
            adapter.addList(list)
        }

        viewModel.onFailProduct.observe(this@MainActivity) { message ->
            bind.dataNotFound = message
            bind.tvDataNotFound.visibility = View.VISIBLE
        }
    }
}