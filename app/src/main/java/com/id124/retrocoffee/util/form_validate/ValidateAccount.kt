package com.id124.retrocoffee.util.form_validate

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.id124.retrocoffee.util.Utils.Companion.isValidEmail

class ValidateAccount {
    companion object {
        fun valName(inName: TextInputLayout, etName: EditText): Boolean {
            val text = etName.text.toString().trim()

            if (text.isEmpty()) {
                inName.isHelperTextEnabled = true
                inName.helperText = "Please enter your name!"
                etName.requestFocus()

                return false
            } else {
                inName.isHelperTextEnabled = false
            }

            return true
        }

    fun valEmail(inEmail: TextInputLayout, etEmail: EditText): Boolean {
        val text = etEmail.text.toString().trim()

        if (!isValidEmail(text)) {
            inEmail.isHelperTextEnabled = true
            inEmail.helperText = "Please enter valid email!"
            etEmail.requestFocus()

            return false
        } else {
            inEmail.isHelperTextEnabled = false
        }

        return true
    }

    fun valPhoneNumber(inPhoneNumber: TextInputLayout, etPhoneNumber: EditText): Boolean {
        val text = etPhoneNumber.text.toString().trim()

        if (text.isEmpty()) {
            inPhoneNumber.isHelperTextEnabled = true
            inPhoneNumber.helperText = "Please enter your phone number!"
            etPhoneNumber.requestFocus()

            return false
        } else {
            inPhoneNumber.isHelperTextEnabled = false
        }

        return true
    }

    fun valCompany(inCompany: TextInputLayout, etCompany: EditText): Boolean {
        val text = etCompany.text.toString().trim()

        if (text.isEmpty()) {
            inCompany.isHelperTextEnabled = true
            inCompany.helperText = "Please enter your company name!"
            etCompany.requestFocus()

            return false
        } else {
            inCompany.isHelperTextEnabled = false
        }

        return true
    }

    fun valPosition(inPosition: TextInputLayout, etPosition: EditText): Boolean {
        val text = etPosition.text.toString().trim()

        if (text.isEmpty()) {
            inPosition.isHelperTextEnabled = true
            inPosition.helperText = "Please enter your position in company!"
            etPosition.requestFocus()

            return false
        } else {
            inPosition.isHelperTextEnabled = false
        }

        return true
    }

    fun valPassword(inPass: TextInputLayout, etPass: EditText): Boolean {
        val text = etPass.text.toString().trim()

        when {
            text.isEmpty() -> {
                inPass.isHelperTextEnabled = true
                inPass.helperText = "Please enter your password!"
                etPass.requestFocus()

                return false
            }
            text.length < 6 -> {
                inPass.isHelperTextEnabled = true
                inPass.helperText = "Min. 6 character!"
                etPass.requestFocus()

                return false
            }
            else -> {
                inPass.isHelperTextEnabled = false
            }
        }

        return true
    }

    fun valPassConf(inPassConf: TextInputLayout, etPassConf: EditText, etPass: EditText): Boolean {
        val textConf = etPassConf.text.toString().trim()
        val text = etPass.text.toString().trim()

        when {
            textConf.isEmpty() -> {
                inPassConf.isHelperTextEnabled = true
                inPassConf.helperText = "Please enter your confirmation password!"
                etPassConf.requestFocus()

                return false
            }
            textConf != text -> {
                inPassConf.isHelperTextEnabled = true
                inPassConf.helperText = "Confirmation password not matches!"
                etPassConf.requestFocus()

                return false
            }
            else -> {
                inPassConf.isHelperTextEnabled = false
            }
        }

        return true
     }
    }
}
