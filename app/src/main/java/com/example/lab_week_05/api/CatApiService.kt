package com.example.lab_week_05.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CatApiService {
    @GET("images/search")
    fun searchImages(
        @Header("x-api-key") apiKey: String, // tambahkan API key di sini
        @Query("limit") limit: Int,
        @Query("size") format: String
    ): Call<String>
}
