package com.id124.retrocoffee.activity.customer.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.navigation.NavigationView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.cart.CartActivity
import com.id124.retrocoffee.activity.customer.main.fragment.ProductFragment
import com.id124.retrocoffee.activity.customer.profile.ProfileActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityMainBinding
import com.id124.retrocoffee.util.ViewPagerAdapter

class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_main
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setNavigationDrawer()
        setViewModel()
        subscribeLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ln_search -> {

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
                Log.d("msg", "Orders")
            }
            R.id.nav_all_menu -> {
                Log.d("msg", "All Menu")
            }
            R.id.nav_privacy -> {
                Log.d("msg", "Privacy")
            }
            R.id.nav_security -> {
                Log.d("msg", "Security")
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
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = this.resources.getColor(R.color.background, theme)

        setSupportActionBar(bind.toolbar)
        supportActionBar?.title = ""
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

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
        viewModel.setService(createApi(this@MainActivity))
        viewModel.serviceGetApi()
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this@MainActivity, {
            if (it) {
                Log.d("msg", "Show Loading")
            } else {
                Log.d("msg", "Hide Loading")
            }
        })

        viewModel.onSuccessLiveData.observe(this@MainActivity, { list ->
            bind.tabLayout.setupWithViewPager(bind.viewPager)
            val adapter = ViewPagerAdapter(supportFragmentManager)

            list.map {
                Log.d("msg", it.ctName)
                adapter.addFrag(ProductFragment(it.ctId), it.ctName)
            }

            bind.viewPager.adapter = adapter
        })

        viewModel.onFailLiveData.observe(this@MainActivity, {
            noticeToast(it)
        })
    }
}