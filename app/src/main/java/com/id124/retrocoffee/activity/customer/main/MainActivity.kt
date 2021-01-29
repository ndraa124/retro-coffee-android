package com.id124.retrocoffee.activity.customer.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
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
import com.id124.retrocoffee.activity.customer.product_search.ProductSearchActivity
import com.id124.retrocoffee.activity.customer.profile.ProfileActivity
import com.id124.retrocoffee.activity.customer.promote.PromoteActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityMainBinding
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.ViewPagerAdapter
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_main
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setNavigationDrawer()
        setNavigationDrawerHeader()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ln_search -> {
                startActivity(Intent(this, ProductSearchActivity::class.java))
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

        if (sharedPref.getCsPicImage() == "") {
            ivProfile.setImageResource(R.drawable.profile)
        } else {
            Glide.with(this@MainActivity)
                .asBitmap()
                .load(BASE_URL_IMAGE + sharedPref.getCsPicImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
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

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
        viewModel.setService(createApi(this@MainActivity))
        viewModel.serviceGetApi()
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this@MainActivity) {
            if (it) {
                Log.d("msg", "Show Loading")
            } else {
                Log.d("msg", "Hide Loading")
            }
        }

        viewModel.onSuccessLiveData.observe(this@MainActivity) { list ->
            bind.tabLayout.setupWithViewPager(bind.viewPager)
            val adapter = ViewPagerAdapter(supportFragmentManager)

            list.map {
                Log.d("msg", it.ctName)
                adapter.addFrag(ProductFragment(it.ctId), it.ctName)
            }

            bind.viewPager.adapter = adapter
        }

        viewModel.onFailLiveData.observe(this@MainActivity) {
            noticeToast(it)
        }
    }
}