package com.id124.retrocoffee.activity.admin.order.detail_order

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.admin.order.detail_order.adapter.HistoryAdapter
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityDetailOrderBinding
import com.id124.retrocoffee.util.Utils
import com.id124.retrocoffee.util.Utils.Companion.formatDate1
import com.id124.retrocoffee.util.Utils.Companion.formatDate2

class DetailOrderActivity : BaseActivity<ActivityDetailOrderBinding>(), View.OnClickListener {
    private lateinit var viewModel: DetailOrderViewModel
    private lateinit var adapter: HistoryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var subtotal: Long = 0
    private var total: Long = 0
    private var fee: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_detail_order
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setDataFromIntent()
        setProductRecyclerView()
        setViewModel()
        subscribeLiveData()
        subscribeOrderLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_process -> {
                viewModel.serviceUpdateApi(
                    csId = intent.getIntExtra("cs_id", 0),
                    orId = intent.getIntExtra("or_id", 0),
                    orStatus = 1
                )
            }
            R.id.btn_cancel -> {
                viewModel.serviceUpdateApi(
                    csId = intent.getIntExtra("cs_id", 0),
                    orId = intent.getIntExtra("or_id", 0),
                    orStatus = 2
                )
            }
            R.id.btn_delivery -> {
                viewModel.serviceUpdateApi(
                    csId = intent.getIntExtra("cs_id", 0),
                    orId = intent.getIntExtra("or_id", 0),
                    orStatus = 3
                )
            }
            R.id.btn_done -> {
                viewModel.serviceUpdateApi(
                    csId = intent.getIntExtra("cs_id", 0),
                    orId = intent.getIntExtra("or_id", 0),
                    orStatus = 4
                )
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

    private fun setDataFromIntent() {
        val orderId = intent.getIntExtra("or_id", 0)
        val orderDate = intent.getStringExtra("or_date_order")
        val noTransaction = formatDate1(orderDate!!)

        bind.ordersNumber = "RCS-${orderId}${noTransaction}"
        bind.dateOrders = formatDate2(intent.getStringExtra("or_date_order")!!)
        bind.address = intent.getStringExtra("or_address")

        when (intent.getIntExtra("or_status", 0)) {
            0 -> {
                bind.btnProcess.visibility = View.VISIBLE
                bind.btnCancel.visibility = View.VISIBLE
                bind.btnDelivery.visibility = View.GONE
                bind.btnDone.visibility = View.GONE

                bind.tvStatus.setTextColor(getColor(R.color.secondary))
                bind.status = "Waiting"
            }
            1 -> {
                if (intent.getStringExtra("or_note_approve") == "Door Delivery") {
                    bind.btnDelivery.visibility = View.VISIBLE
                    bind.btnDone.visibility = View.GONE
                } else {
                    bind.btnDelivery.visibility = View.GONE
                    bind.btnDone.visibility = View.VISIBLE
                }

                bind.btnProcess.visibility = View.GONE
                bind.btnCancel.visibility = View.GONE

                bind.tvStatus.setTextColor(getColor(R.color.design_default_color_primary_variant))
                bind.status = "Proceed"
            }
            2 -> {
                bind.btnProcess.visibility = View.GONE
                bind.btnCancel.visibility = View.GONE
                bind.btnDelivery.visibility = View.GONE
                bind.btnDone.visibility = View.GONE

                bind.tvStatus.setTextColor(getColor(R.color.design_default_color_error))
                bind.status = "Reject"
            }
            3 -> {
                bind.btnProcess.visibility = View.GONE
                bind.btnCancel.visibility = View.GONE
                bind.btnDelivery.visibility = View.GONE
                bind.btnDone.visibility = View.VISIBLE

                bind.tvStatus.setTextColor(getColor(R.color.design_default_color_secondary_variant))
                bind.status = "Delivery"
            }
            4 -> {
                bind.btnProcess.visibility = View.GONE
                bind.btnCancel.visibility = View.GONE
                bind.btnDelivery.visibility = View.GONE
                bind.btnDone.visibility = View.GONE

                bind.tvStatus.setTextColor(getColor(R.color.green))
                bind.status = "Done"
            }
        }

        when (intent.getStringExtra("or_note_approve")) {
            "Dine In" -> {
                bind.btnDineIn.setTextColor(resources.getColor(R.color.white, theme))
                bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnPickUp.setTextColor(resources.getColor(R.color.gray_500, theme))

                bind.btnDineIn.isPressed = true
                bind.btnDoorDelivery.isPressed = false
                bind.btnPickUp.isPressed = false
            }
            "Door Delivery" -> {
                bind.btnDineIn.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.white, theme))
                bind.btnPickUp.setTextColor(resources.getColor(R.color.gray_500, theme))

                bind.btnDineIn.isPressed = false
                bind.btnDoorDelivery.isPressed = true
                bind.btnPickUp.isPressed = false
            }
            "Pick Up" -> {
                bind.btnDineIn.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnDoorDelivery.setTextColor(resources.getColor(R.color.gray_500, theme))
                bind.btnPickUp.setTextColor(resources.getColor(R.color.white, theme))

                bind.btnDineIn.isPressed = false
                bind.btnDoorDelivery.isPressed = false
                bind.btnPickUp.isPressed = true
            }
        }

        when (intent.getStringExtra("or_method_payment")) {
            "Card" -> {
                bind.rbCard.visibility = View.VISIBLE
                bind.rbBankAccount.visibility = View.GONE
                bind.rbCod.visibility = View.GONE
            }
            "Bank account" -> {
                bind.rbCard.visibility = View.GONE
                bind.rbBankAccount.visibility = View.VISIBLE
                bind.rbCod.visibility = View.GONE
            }
            "Cash on delivery" -> {
                bind.rbCard.visibility = View.GONE
                bind.rbBankAccount.visibility = View.GONE
                bind.rbCod.visibility = View.VISIBLE
            }
        }
    }

    private fun setProductRecyclerView() {
        layoutManager = LinearLayoutManager(this@DetailOrderActivity, RecyclerView.VERTICAL, false)
        bind.rvProduct.layoutManager = layoutManager

        adapter = HistoryAdapter()
        bind.rvProduct.adapter = adapter
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this@DetailOrderActivity).get(DetailOrderViewModel::class.java)
        viewModel.setService(createApi(this@DetailOrderActivity))
        viewModel.setServiceOrder(createApi(this@DetailOrderActivity))
        viewModel.serviceGetApi(
            orId = intent.getIntExtra("or_id", 0)
        )
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@DetailOrderActivity) {
            if (it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccess.observe(this@DetailOrderActivity) { list ->
            for (i in list.indices) {
                subtotal += list[i].htTotal!!
            }

            adapter.addList(list)
            bind.tvDataNotFound.visibility = View.GONE

            setPayTotal()
        }

        viewModel.onFail.observe(this@DetailOrderActivity) { message ->
            bind.tvDataNotFound.text = message
            bind.tvDataNotFound.visibility = View.VISIBLE
        }
    }

    private fun subscribeOrderLiveData() {
        viewModel.isLoadingOrder.observe(this@DetailOrderActivity) {
            if (it) {
                bind.btnProcess.visibility = View.GONE
                bind.btnCancel.visibility = View.GONE
                bind.btnDelivery.visibility = View.GONE
                bind.btnDone.visibility = View.GONE

                bind.progressBarOrder.visibility = View.VISIBLE
            } else {
                bind.progressBarOrder.visibility = View.GONE
            }
        }

        viewModel.onSuccessOrder.observe(this@DetailOrderActivity) {
            if (it) {
                this@DetailOrderActivity.finish()
            }
        }

        viewModel.onFailOrder.observe(this@DetailOrderActivity) { message ->
            noticeToast(message)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPayTotal() {
        fee = 5000
        total = subtotal + fee

        bind.tvIdrTotal.text = Utils.currencyFormat(subtotal.toString())
        bind.tvTaxTotal.text = Utils.currencyFormat(fee.toString())
        bind.tvPayTotal.text = Utils.currencyFormat(total.toString())
    }
}