package com.id124.retrocoffee.activity.customer.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.loader.content.CursorLoader
import com.id124.retrocoffee.R
import com.id124.retrocoffee.activity.customer.main.MainActivity
import com.id124.retrocoffee.base.BaseActivity
import com.id124.retrocoffee.databinding.ActivityEditProfileBinding
import com.id124.retrocoffee.remote.ApiClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var deadlineProject: DatePickerDialog.OnDateSetListener
    private lateinit var c: Calendar
    private var gender: Int = 0

    companion object {
        private const val IMAGE_PICK_CODE = 100
        private const val PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_edit_profile
        super.onCreate(savedInstanceState)
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

        bind.editImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions,
                        PERMISSION_CODE
                    )
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }
        deadlineProject()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickImage()
                }
                else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            bind.imageProfile.setImageURI(data?.data)

            val filePath = data?.data?.let { getPath(this, it) }
            val file = File(filePath)
            Log.d("FileName", file.name)

            var img: MultipartBody.Part? = null
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", file.name, it1)
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

                if (img != null) {
                    viewModel.updateAccount(acId, name, email, phone)
                    viewModel.updateCustomerWithImage(csId, gender, dob, address, img)
                }

            }


        }
    }

    private fun getPath(context: Context, contentUri: Uri) : String? {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }

    private fun datePicker(){
        DatePickerDialog(
            this, deadlineProject, c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun deadlineProject() {
        deadlineProject = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val day = findViewById<TextView>(R.id.tv_dob)
            val formatDate = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(formatDate, Locale.US)

            day.text = sdf.format(c.time)
        }
    }


    private fun setToolbar(){
        bind.toolbar.title = "Edit Profile"
        setSupportActionBar(bind.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bind.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setViewModel(){
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        viewModel.setServiceProfile(createApi(this))
        viewModel.setServiceAccount(createApi(this))
        viewModel.getCustomerById(csId = sharedPref.getCsId())
    }

    private fun subcsribeLiveData(){
        viewModel.isLoading.observe(this) {
            if(it) {
                bind.progressBar.visibility = View.VISIBLE
            } else {
                bind.progressBar.visibility = View.GONE
            }
        }

        viewModel.onSuccessUpdate.observe(this) {
            if(it) {
                intents<MainActivity>(this)
                Toast.makeText(this, "Success to update account", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Failed to update account", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.listProfile.observe(this) {
            bind.model = it[0]
            val dob = it[0].csDob.split("T")[0].toString()
            bind.tvDob.text = dob
            bind.imageUrl = ApiClient.BASE_URL_IMAGE + it[0].csImage

            val gender = it[0].csGender

            if (gender == 1) {
                val b = findViewById<View>(R.id.male) as RadioButton
                b.isChecked = true
            } else {
                val b = findViewById<View>(R.id.female) as RadioButton
                b.isChecked = true
            }
        }
    }

}