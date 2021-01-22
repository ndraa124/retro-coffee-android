package com.id124.retrocoffee.activity.customer.splash

interface SplashContract {

    interface View {
        fun setCountdown()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
    }

}