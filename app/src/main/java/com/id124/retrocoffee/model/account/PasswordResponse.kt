package com.id124.retrocoffee.model.account

data class PasswordResponse(
    val message: String,
    val success: Boolean,
    val data: itemData
){
    data class itemData(
        val acId : Int,
        val password : String
    )

}
