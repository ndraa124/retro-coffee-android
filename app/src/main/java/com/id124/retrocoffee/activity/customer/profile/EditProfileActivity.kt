package com.id124.retrocoffee.activity.customer.profile

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityEditProfileBinding
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(), View.OnClickListener {
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var myCalendar: Calendar
    private lateinit var dateOfBirth: OnDateSetListener

    companion object {
        private const val IMAGE_PICK_CODE = 100
        private const val PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_edit_profile
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setDataSharedPref()

        myCalendar = Calendar.getInstance()
        dateOfBirth()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.et_dob -> {
                DatePickerDialog(
                    this@EditProfileActivity, dateOfBirth, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.iv_edit_profile -> {

            }
            R.id.btn_save -> {

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

    private fun setDataSharedPref() {
        if (sharedPref.getCsPicImage() == null) {
            bind.ivImageProfile.setImageResource(R.drawable.profile)
        } else {
            bind.imageUrl = BASE_URL_IMAGE + sharedPref.getCsPicImage()
        }

        bind.etName.setText(sharedPref.getAcName())
        bind.etEmail.setText(sharedPref.getAcEmail())
        bind.etPhoneNumber.setText(sharedPref.getAcPhone())

        if (sharedPref.getCsGender() != null) {
            when {
                sharedPref.getCsGender() == "1" -> {
                    bind.rbMale.isChecked = true
                }
                else -> {
                    bind.rbFemale.isChecked = true
                }
            }
        }

        if (sharedPref.getCsDateOfBirth() != null) {
            bind.etDob.setText(sharedPref.getCsDateOfBirth())
        }

        if (sharedPref.getCsAddress() != null) {
            bind.etAddress.setText(sharedPref.getCsAddress())
        }
    }

    private fun dateOfBirth() {
        dateOfBirth = OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val day = findViewById<TextView>(R.id.et_dob)
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            day.text = sdf.format(myCalendar.time)
        }
    }
}