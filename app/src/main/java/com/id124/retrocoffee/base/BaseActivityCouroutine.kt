package com.id124.retrocoffee.base

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.id124.retrocoffee.base.BaseActivity.Companion.IMAGE_PICK_CODE
import com.id124.retrocoffee.remote.ApiClient
import com.id124.retrocoffee.util.SharedPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseActivityCoroutine<ActivityBinding : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var bind: ActivityBinding
    protected lateinit var sharedPref: SharedPreference
    protected lateinit var userDetail: HashMap<String, String>
    protected lateinit var coroutineScope: CoroutineScope
    protected var setLayout: Int? = null
    protected var pathImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this@BaseActivityCoroutine, setLayout!!)

        sharedPref = SharedPreference(this@BaseActivityCoroutine)
        userDetail = sharedPref.getAccountUser()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    }

    protected inline fun <reified ClassActivity> intents(context: Context) {
        context.startActivity(Intent(context, ClassActivity::class.java))
    }

    protected inline fun <reified ClassActivity> intentsResults(context: Context, requestCode: Int) {
        startActivityForResult(Intent(context, ClassActivity::class.java), requestCode)
    }

    protected inline fun <reified ApiService> createApi(context: Context): ApiService {
        return ApiClient.getApiClient(context).create(ApiService::class.java)
    }

    protected fun noticeToast(message: String) {
        Toast.makeText(this@BaseActivityCoroutine, message, Toast.LENGTH_SHORT).show()
    }

    protected fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}