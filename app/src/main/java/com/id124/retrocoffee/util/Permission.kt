package com.id124.retrocoffee.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.id124.retrocoffee.R

class Permission(private val mActivity: Activity) {
    private var listener: PermissionListener? = null

    companion object {
        private const val REQUEST_PERMISSION = 99
    }

    fun permissionListener(permissionListener: PermissionListener?) {
        listener = permissionListener
    }

    fun checkAndRequestPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionReadStorage = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionWriteStorage = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val listPermissionsNeeded: MutableList<String> = ArrayList()

            if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            if (listPermissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(mActivity, listPermissionsNeeded.toTypedArray(), REQUEST_PERMISSION)
                return false
            }
        }

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        listener!!.onPermissionCheckDone()

        return true
    }

    fun onRequestCallBack(RequestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (RequestCode) {
            REQUEST_PERMISSION -> {
                val perms: MutableMap<String, Int> = HashMap()

                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }

                    if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                        checkAndRequestPermissions()
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        ) {
                            showDialogOK(mActivity.getString(R.string.permission_dialog)) { _, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(mActivity)
            .setMessage(message)
            .setPositiveButton(
                "OK",
                okListener
            )
            .setCancelable(false)
            .create()
            .show()
    }

    interface PermissionListener {
        fun onPermissionCheckDone()
    }
}