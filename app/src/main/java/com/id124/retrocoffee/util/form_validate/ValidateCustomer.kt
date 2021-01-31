package com.id124.retrocoffee.util.form_validate

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

class ValidateCustomer {
    companion object {
        fun valDob(inName: TextInputLayout, etDob: EditText): Boolean {
            val text = etDob.text.toString().trim()

            if (text.isEmpty()) {
                inName.isHelperTextEnabled = true
                inName.helperText = "Please enter your date of birth!"
                etDob.requestFocus()

                return false
            } else {
                inName.isHelperTextEnabled = false
            }

            return true
        }

        fun valDeliveryAddress(inEmail: TextInputLayout, etAddress: EditText): Boolean {
            val text = etAddress.text.toString().trim()

            if (text.isEmpty()) {
                inEmail.isHelperTextEnabled = true
                inEmail.helperText = "Please enter your delivery address!"
                etAddress.requestFocus()

                return false
            } else {
                inEmail.isHelperTextEnabled = false
            }

            return true
        }
    }
}
