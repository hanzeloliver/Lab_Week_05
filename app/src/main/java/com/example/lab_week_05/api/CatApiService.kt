package com.example.lab_week_05.api

import com.example.lab_week_05.model.ImageData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CatApiService {
    @GET("images/search")
    fun searchImages(
        @Header("x-api-key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("size") format: String
    ): Call<List<ImageData>>
}
