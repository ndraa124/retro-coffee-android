package com.id124.retrocoffee.util.form_validate

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

class ValidateProduct {
    companion object {
        fun valName(inName: TextInputLayout, etDob: EditText): Boolean {
            val text = etDob.text.toString().trim()

            if (text.isEmpty()) {
                inName.isHelperTextEnabled = true
                inName.helperText = "Please enter product name!"
                etDob.requestFocus()

                return false
            } else {
                inName.isHelperTextEnabled = false
            }

            return true
        }

        fun valPrice(inPrice: TextInputLayout, etPrice: EditText): Boolean {
            val text = etPrice.text.toString().trim()

            if (text.isEmpty()) {
                inPrice.isHelperTextEnabled = true
                inPrice.helperText = "Please enter price!"
                etPrice.requestFocus()

                return false
            } else {
                inPrice.isHelperTextEnabled = false
            }

            return true
        }

        fun valDescription(inDesc: TextInputLayout, etDesc: EditText): Boolean {
            val text = etDesc.text.toString().trim()

            if (text.isEmpty()) {
                inDesc.isHelperTextEnabled = true
                inDesc.helperText = "Please enter product description!"
                etDesc.requestFocus()

                return false
            } else {
                inDesc.isHelperTextEnabled = false
            }

            return true
        }

        fun valPercent(inPercent: TextInputLayout, etPercent: EditText): Boolean {
            val text = etPercent.text.toString().trim()

            if (text.isEmpty()) {
                inPercent.isHelperTextEnabled = true
                inPercent.helperText = "Please enter total percent!"
                etPercent.requestFocus()

                return false
            } else {
                inPercent.isHelperTextEnabled = false
            }

            return true
        }
    }
}
