package com.id124.retrocoffee.model.account

//data class EmailModel()

data class EmailModel(
    val data: EmailResponse,
    val message: String,
    val success: Boolean)
{
    data class EmailResponse(
        val ac_created_at: String,
        val ac_email: String,
        val ac_id: Int,
        val ac_level: Int,
        val ac_name: String,
        val ac_password: String,
        val ac_phone: String,
        val ac_status: Int,
        val ac_updated_at: String
    )
}