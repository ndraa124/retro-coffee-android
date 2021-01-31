package com.id124.retrocoffee.activity.customer.profile

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.id124.retrocoffee.R
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityEditProfileBinding
import com.id124.retrocoffee.remote.ApiClient.Companion.BASE_URL_IMAGE
import com.id124.retrocoffee.util.FileHelper
import com.id124.retrocoffee.util.FileHelper.Companion.createPartFromFile
import com.id124.retrocoffee.util.FileHelper.Companion.createPartFromString
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valEmail
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valName
import com.id124.retrocoffee.util.form_validate.ValidateAccount.Companion.valPhoneNumber
import com.id124.retrocoffee.util.form_validate.ValidateCustomer.Companion.valDeliveryAddress
import com.id124.retrocoffee.util.form_validate.ValidateCustomer.Companion.valDob
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(), View.OnClickListener {
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var myCalendar: Calendar
    private lateinit var dateOfBirth: OnDateSetListener
    private var pathImage: String? = null
    private var gender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_edit_profile
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        c = Calendar.getInstance()

        setToolbar()
        setViewModel()
        subcsribeLiveData()

        bind.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.male) {
                sharedPref.createCsGender(1)
                Log.d("gender", sharedPref.getCsGender().toString())
                Toast.makeText(this, sharedPref.getCsGender().toString() , Toast.LENGTH_SHORT).show()
            } else if (i == R.id.female) {
                sharedPref.createCsGender(0)
                Log.d("gender", sharedPref.getCsGender().toString())
                Toast.makeText(this, sharedPref.getCsGender().toString() , Toast.LENGTH_SHORT).show()
            } else {
                Log.d("error", "error")
            }
        }

        bind.tvDob.setOnClickListener {
            datePicker()
        }

        bind.btnDob.setOnClickListener {
            datePicker()
        }

        bind.btnSave.setOnClickListener {
            val acId = sharedPref.getAcId()
            val csId = sharedPref.getCsId()
            val name = bind.etName.text.toString()
            val email = bind.etEmail.text.toString()
            val phone = bind.etPhone.text.toString()
            val gender = sharedPref.getCsGender().toString()
            val dob = bind.tvDob.text.toString()
            val address = bind.etAddress.text.toString()

            viewModel.updateAccount(acId, name, email, phone)
            viewModel.updateCustomer(csId, gender, dob, address)
        }
=======

        setToolbarActionBar()
        setDataSharedPref()
        initTextWatcher()

        myCalendar = Calendar.getInstance()
        dateOfBirth()

        setViewModel()
        subscribeLiveData()
    }
>>>>>>> ca356e2c37b37462168333b0f6c9b99dad5d27f8

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
                pickImageFromGallery()
            }
            R.id.btn_save -> {
                when {
                    !valName(bind.inputLayoutName, bind.etName) -> {
                    }
                    !valEmail(bind.inputLayoutEmail, bind.etEmail) -> {
                    }
                    !valPhoneNumber(bind.inputLayoutPhoneNumber, bind.etPhoneNumber) -> {
                    }
                    !valDob(bind.inputLayoutDob, bind.etDob) -> {
                    }
                    !valDeliveryAddress(bind.inputLayoutAddress, bind.etAddress) -> {
                    }
                    else -> {
                        when (bind.rgGender.checkedRadioButtonId) {
                            bind.rbFemale.id -> {
                                gender = "0"
                            }
                            bind.rbMale.id -> {
                                gender = "1"
                            }
                        }

                        if (gender == null) {
                            noticeToast("Please choose your gender first!")
                        } else {
                            if (pathImage == null) {
                                viewModel.updateCustomer(
                                    csId = sharedPref.getCsId(),
                                    csGender = createPartFromString(gender!!),
                                    csDob = createPartFromString(bind.etDob.text.toString()),
                                    csAddress = createPartFromString(bind.etAddress.text.toString())
                                )

                                viewModel.updateAccount(
                                    acId = sharedPref.getAcId(),
                                    acName = bind.etName.text.toString(),
                                    acEmail = bind.etEmail.text.toString(),
                                    acPhone = bind.etPhoneNumber.text.toString()
                                )
                            } else {
                                viewModel.updateCustomer(
                                    csId = sharedPref.getCsId(),
                                    csGender = createPartFromString(gender!!),
                                    csDob = createPartFromString(bind.etDob.text.toString()),
                                    csAddress = createPartFromString(bind.etAddress.text.toString()),
                                    image = createPartFromFile(pathImage!!)
                                )

                                viewModel.updateAccount(
                                    acId = sharedPref.getAcId(),
                                    acName = bind.etName.text.toString(),
                                    acEmail = bind.etEmail.text.toString(),
                                    acPhone = bind.etPhoneNumber.text.toString()
                                )
                            }
                        }
                    }
                }
            }
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    noticeToast("Permission denied...!")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            bind.ivImageProfile.setImageURI(data?.data)
            pathImage = FileHelper.getPathFromURI(this@EditProfileActivity, data?.data!!)
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
        if (sharedPref.getCsPicImage() == null || sharedPref.getCsPicImage() == "") {
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
            bind.etDob.setText(sharedPref.getCsDateOfBirth()!!.split('T')[0])
        }

        if (sharedPref.getCsAddress() != null) {
            bind.etAddress.setText(sharedPref.getCsAddress())
        }
    }

    private fun initTextWatcher() {
        bind.etName.addTextChangedListener(MyTextWatcher(bind.etName))
        bind.etEmail.addTextChangedListener(MyTextWatcher(bind.etEmail))
        bind.etPhoneNumber.addTextChangedListener(MyTextWatcher(bind.etPhoneNumber))
        bind.etDob.addTextChangedListener(MyTextWatcher(bind.etDob))
        bind.etAddress.addTextChangedListener(MyTextWatcher(bind.etAddress))
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

    private fun setViewModel() {
        viewModel =
            ViewModelProvider(this@EditProfileActivity).get(EditProfileViewModel::class.java)
        viewModel.setServiceAccount(createApi(this@EditProfileActivity))
        viewModel.setServiceCustomer(createApi(this@EditProfileActivity))
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(this@EditProfileActivity) {
            if (it) {
                bind.btnSave.visibility = View.GONE
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
                bind.btnSave.visibility = View.VISIBLE
            }
        }

        viewModel.onSuccess.observe(this@EditProfileActivity, { success ->
            if (success) {
                viewModel.onSuccessCs.observe(this@EditProfileActivity, {
                    if (it == "" || it == null) {
                        sharedPref.createCsPicImage("")
                    } else {
                        sharedPref.createCsPicImage(it)
                    }
                })

                sharedPref.createAcName(bind.etName.text.toString())
                sharedPref.createAcEmail(bind.etEmail.text.toString())
                sharedPref.createAcPhone(bind.etPhoneNumber.text.toString())
                sharedPref.createCsGender(gender!!)
                sharedPref.createCsDateOfBirth(bind.etDob.text.toString())
                sharedPref.createAddress(bind.etAddress.text.toString())

                this@EditProfileActivity.finish()
            }
        })

        viewModel.onFail.observe(this@EditProfileActivity, { message ->
            noticeToast(message)
        })
    }

    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_name -> valName(bind.inputLayoutName, bind.etName)
                R.id.et_email -> valEmail(bind.inputLayoutEmail, bind.etEmail)
                R.id.et_phone_number -> valPhoneNumber(
                    bind.inputLayoutPhoneNumber,
                    bind.etPhoneNumber
                )
                R.id.et_dob -> valDob(bind.inputLayoutDob, bind.etDob)
                R.id.et_address -> valDeliveryAddress(bind.inputLayoutAddress, bind.etAddress)
            }
        }
    }
}