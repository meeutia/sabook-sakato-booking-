package com.example.sebook.data.model

import com.squareup.moshi.Json

data class LoginResponse(

	@field:Json(name="success")
	val success: Boolean,

	@field:Json(name="id_user")
	val idUser: Int?,

	@field:Json(name="message")
	val message: String,

	@field:Json(name="token")
	val token: String?
)