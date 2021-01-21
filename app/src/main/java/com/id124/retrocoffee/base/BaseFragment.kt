package com.id124.retrocoffee.base

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.id124.retrocoffee.remote.ApiClient
import com.id124.retrocoffee.util.SharedPreference

abstract class BaseFragment<FragmentBinding : ViewDataBinding> : Fragment() {
    protected lateinit var bind: FragmentBinding
    protected lateinit var sharedPref: SharedPreference
    protected var setLayout: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater, setLayout!!, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPreference(view.context)
    }

    protected inline fun <reified ClassActivity> intents(activity: FragmentActivity?) {
        activity?.startActivity(Intent(activity, ClassActivity::class.java))
    }

    protected inline fun <reified ClassActivity> intentsResults(activity: FragmentActivity?, requestCode: Int) {
        activity?.startActivityForResult(Intent(activity, ClassActivity::class.java), requestCode)
    }

    protected inline fun <reified ApiService> createApi(activity: FragmentActivity?): ApiService {
        return activity?.let { ApiClient.getApiClient(it).create(ApiService::class.java) }!!
    }

    protected fun noticeToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    protected fun logoutConfirmation(activity: FragmentActivity?) {
        val dialog = activity?.let {
            AlertDialog
                .Builder(it)
                .setTitle("Notice!")
                .setMessage("Are you sure to logout?")
                .setPositiveButton("OK") { _, _ ->
                    sharedPref.accountLogout()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
        }

        dialog?.show()
    }
}