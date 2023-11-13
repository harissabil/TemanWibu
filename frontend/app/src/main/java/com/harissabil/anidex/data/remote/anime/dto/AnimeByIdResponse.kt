package com.harissabil.anidex.data.remote.anime.dto

import com.google.gson.annotations.SerializedName

data class AnimeByIdResponse(

	@field:SerializedName("data")
	val data: Data
)