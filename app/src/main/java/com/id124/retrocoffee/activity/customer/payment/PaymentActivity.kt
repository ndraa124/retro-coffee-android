package com.id124.retrocoffee.activity.customer.payment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.history.HistoryActivity
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.activity.customer.payment.adapter.CardSliderAdapter
import com.id124.retrocoffee.activity.customer.payment.adapter.CartAdapter
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityPaymentBinding
import com.id124.retrocoffee.model.slider.SliderModel
import com.id124.retrocoffee.util.Utils.Companion.currencyFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


class PaymentActivity : BaseActivity<ActivityPaymentBinding>(), View.OnClickListener {
    private lateinit var viewModel: PaymentViewModel
    private lateinit var adapter: CartAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var paymentMethod: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_payment
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setProductRecyclerView()
        setCardSlider()
        setViewModel()
        subscribeLiveData()
        setPayTotal()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_proceed_payment -> {
                when (bind.rgMethodPayment.checkedRadioButtonId) {
                    bind.rbCard.id -> {
                        paymentMethod = "Card"
                    }
                    bind.rbBankAccount.id -> {
                        paymentMethod = "Bank account"
                    }
                    bind.rbCod.id -> {
                        paymentMethod = "Cash on delivery"
                    }
                }

                if (paymentMethod == null) {
                    noticeToast("Please choose payment method!")
                } else {
                    confirmTransaction()
                }
            }
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }

    private fun setToolbarActionBar() {
        setStatusBar()
        setSupportActionBar(bind.toolbar)
    }

    private fun setStatusBar() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = this.resources.getColor(R.color.background, theme)
    }

    private fun setCardSlider() {
        val sliderModel = mutableListOf<SliderModel>()
        sliderModel.add(SliderModel(R.drawable.img_card_account))
        sliderModel.add(SliderModel(R.drawable.img_card_account))
        sliderModel.add(SliderModel(R.drawable.img_card_account))

        val adapter = CardSliderAdapter(sliderModel, bind.viewPager)
        bind.viewPager.adapter = adapter

        bind.viewPager.clipToPadding = false
        bind.viewPager.clipChildren = false
        bind.viewPager.offscreenPageLimit = 3
        bind.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r: Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.25f
        }

        bind.viewPager.setPageTransformer(compositePageTransformer)
    }

    private fun setProductRecyclerView() {
        layoutManager = LinearLayoutManager(this@PaymentActivity, RecyclerView.VERTICAL, false)
        bind.rvProduct.layoutManager = layoutManager

        adapter = CartAdapter()
        bind.rvProduct.adapter = adapter
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@PaymentActivity).get(PaymentViewModel::class.java)
        viewModel.setServiceCart(createApi(this@PaymentActivity))
        viewModel.setServiceOrder(createApi(this@PaymentActivity))
        viewModel.serviceGetApi(
            csId = sharedPref.getCsId()
        )
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@PaymentActivity, {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        })

        viewModel.isLoadingOrder.observe(this@PaymentActivity, {
            if (it) {
                bind.clPayment.visibility = View.GONE
                bind.progressBarOrder.visibility = View.VISIBLE
            }
        })

        viewModel.onSuccess.observe(this@PaymentActivity, { list ->
            adapter.addList(list)
            bind.tvDataNotFound.visibility = View.GONE
        })

        viewModel.onSuccessOrder.observe(this@PaymentActivity, {
            if (it) {
                noticeToast("Order success")

                val intent = Intent(this@PaymentActivity, HistoryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("orderTransaction", 1)
                startActivity(intent)
                finish()
            }
        })

        viewModel.onFail.observe(this@PaymentActivity, { message ->
            bind.clPayment.visibility = View.VISIBLE
            bind.tvDataNotFound.text = message
            bind.tvDataNotFound.visibility = View.VISIBLE
        })

        viewModel.onFailOrder.observe(this@PaymentActivity, {
            noticeToast("Order is fail! Please try again later.")

            val intent = Intent(this@PaymentActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        })
    }

    private fun setPayTotal() {
        bind.tvPayTotal.text = currencyFormat(intent.getLongExtra("pay_total", 0).toString())
    }

    private fun confirmTransaction() {
        val dialog = AlertDialog
            .Builder(this@PaymentActivity)
            .setTitle("Notice!")
            .setMessage("Are you sure to process this order?")
            .setPositiveButton("OK") { _, _ ->
                val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    Date()
                )

                viewModel.serviceAddApi(
                    csId = sharedPref.getCsId(),
                    orPayTotal = intent.getLongExtra("pay_total", 0),
                    orAddress = sharedPref.getCsAddress()!!,
                    orNoteApprove = intent.getStringExtra("store")!!,
                    orMethodPayment = paymentMethod!!,
                    orFee = 5000,
                    orDateOrder = date
                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        dialog?.show()
    }
}