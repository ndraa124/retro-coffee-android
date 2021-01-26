package com.id124.retrocoffee.base

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.id124.retrocoffee.remote.ApiClient
import com.id124.retrocoffee.util.SharedPreference

abstract class BaseActivity<ActivityBinding : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var bind: ActivityBinding
    protected lateinit var sharedPref: SharedPreference
    protected var setLayout: Int? = null

    companion object {
        const val IMAGE_PICK_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this@BaseActivity, setLayout!!)

        sharedPref = SharedPreference(this@BaseActivity)
    }

    protected inline fun <reified ClassActivity> intents(context: Context) {
        context.startActivity(Intent(context, ClassActivity::class.java))
    }

    protected inline fun <reified ClassActivity> intentsResults(
        context: Context,
        requestCode: Int
    ) {
        startActivityForResult(Intent(context, ClassActivity::class.java), requestCode)
    }

    protected inline fun <reified ApiService> createApi(context: Context): ApiService {
        return ApiClient.getApiClient(context).create(ApiService::class.java)
    }

    protected fun noticeToast(message: String) {
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
    }

    protected fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    protected fun signOutConfirm() {
        val dialog = AlertDialog
            .Builder(this@BaseActivity)
            .setTitle("Notice!")
            .setMessage("Are you sure to sign out?")
            .setPositiveButton("OK") { _, _ ->
                sharedPref.accountLogout()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        dialog?.show()
    }
}