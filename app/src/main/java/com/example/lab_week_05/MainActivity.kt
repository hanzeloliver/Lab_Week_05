package com.example.lab_week_05

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.lab_week_05.api.CatApiService
import retrofit2.*
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    private val apiResponseView: TextView by lazy {
        findViewById(R.id.api_response)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        val apiKey = "live_XJ33cqEFSI4eOO3jAtZ7XWwZ5pqjLQUW91c44K8rnDzDNUoQggUDF4eALgixBcT8" // masukkan API key kamu di sini

        val call = catApiService.searchImages(apiKey, 5, "full") // minta 5 gambar kucing
        call.enqueue(object : Callback<String> {
            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(MAIN_ACTIVITY, "Failed to get response", t)
                apiResponseView.text = "Error: ${t.message}"
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    apiResponseView.text = response.body()
                } else {
                    Log.e(MAIN_ACTIVITY, "Failed response: ${response.errorBody()?.string().orEmpty()}")
                    apiResponseView.text = "Failed to fetch data"
                }
            }
        })
    }

    companion object {
        const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
    }
}
