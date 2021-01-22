package com.id124.retrocoffee.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.id124.retrocoffee.activity.customer.login.LoginActivity
import com.id124.retrocoffee.activity.customer.main.MainActivity

class SharedPreference(private val context: Context) {
    companion object {
        const val PREF_NAME = "LOGIN"
        const val LOGIN = "IS_LOGIN"
        const val TOKEN = "TOKEN"
        const val AC_ID = "AC_ID"
        const val AC_NAME = "AC_NAME"
        const val AC_EMAIL = "AC_EMAIL"
        const val AC_PHONE = "AC_PHONE"
        const val AC_LEVEL = "AC_LEVEL"
        const val CS_ID = "CS_ID"
        const val CS_GENDER = "CS_GENDER"
        const val CS_DOB = "CS_DOB"
        const val CS_ADDRESS = "CS_ADDRESS"
        const val CS_LATITUDE = "CS_LATITUDE"
        const val CS_LONGITUDE = "CS_LONGITUDE"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun createAccount(
        csId: Int,
        token: String,
        acId: Int,
        acName: String,
        acEmail: String,
        acPhone: String,
        acLevel: Int
    ) {
        editor.putBoolean(LOGIN, true)
        editor.putString(TOKEN, token)
        editor.putInt(AC_ID, acId)
        editor.putString(AC_NAME, acName)
        editor.putString(AC_EMAIL, acEmail)
        editor.putString(AC_PHONE, acPhone)
        editor.putInt(AC_LEVEL, acLevel)
        editor.putInt(CS_ID, csId)
        editor.commit()
    }

    fun createAcName(acName: String) {
        editor.putString(AC_NAME, acName)
        editor.commit()
    }

    fun createAcEmail(acEmail: String) {
        editor.putString(AC_EMAIL, acEmail)
        editor.commit()
    }

    fun createAcPhone(acPhone: String) {
        editor.putString(AC_PHONE, acPhone)
        editor.commit()
    }

    fun createCsGender(acGender: Int) {
        editor.putInt(CS_GENDER, acGender)
        editor.commit()
    }

    fun createCsDateOfBirth(csDob: String) {
        editor.putString(CS_DOB, csDob)
        editor.commit()
    }

    fun getToken(): String {
        return sharedPreferences.getString(TOKEN, "")!!
    }

    fun getAcId(): Int {
        return sharedPreferences.getInt(AC_ID, 0)
    }

    fun getAcName(): String {
        return sharedPreferences.getString(AC_NAME, "")!!
    }

    fun getAcEmail(): String {
        return sharedPreferences.getString(AC_EMAIL, "")!!
    }

    fun getAcPhone(): String {
        return sharedPreferences.getString(AC_PHONE, "")!!
    }

    fun getAcLevel(): Int {
        return sharedPreferences.getInt(AC_LEVEL, 0)
    }

    fun getCsId(): Int {
        return sharedPreferences.getInt(CS_ID, 0)
    }

    fun getCsGender(): Int {
        return sharedPreferences.getInt(CS_GENDER, 0)
    }

    fun getCsDateOfBirth(): String {
        return sharedPreferences.getString(CS_DOB, "")!!
    }

    fun checkIsLogin() {
        if (!sharedPreferences.getBoolean(LOGIN, false)) {
            context.startActivity(Intent(context, LoginActivity::class.java))
            (context as MainActivity).finish()
        }
    }

    fun accountLogout() {
        editor.clear()
        editor.commit()

        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as MainActivity).finish()
    }

    fun getAccountUser(): HashMap<String, String> {
        val user: HashMap<String, String> = HashMap()
        user[AC_NAME] = sharedPreferences.getString(AC_NAME, "Not set")!!
        user[AC_EMAIL] = sharedPreferences.getString(AC_EMAIL, "Not set")!!
        user[TOKEN] = sharedPreferences.getString(TOKEN, "Not set")!!

        return user
    }
}