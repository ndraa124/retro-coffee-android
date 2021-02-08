package com.id124.retrocoffee.util

import android.annotation.SuppressLint
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class Utils {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun formatDate1(dateToFormat: String): String? {
            try {
                return SimpleDateFormat("yyMMdd").format(SimpleDateFormat("yyyy-MM-dd").parse(dateToFormat))
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        }

        @SuppressLint("SimpleDateFormat")
        fun formatDate2(dateToFormat: String): String? {
            try {
                return SimpleDateFormat("EEE, dd MMM yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(dateToFormat))
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        }

        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun currencyFormat(amount: String): String {
            val formatter = DecimalFormat("#,###")
            return "IDR ${formatter.format(amount.toDouble())}"
        }

        class BottomOffsetDecoration(private val mBottomOffset: Int) : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                val dataSize = state.itemCount
                val position: Int = parent.getChildAdapterPosition(view)
                if (dataSize > 0 && position == dataSize - 1) {
                    outRect[0, 0, 0] = mBottomOffset
                } else {
                    outRect[0, 0, 0] = 0
                }
            }
        }
    }
}