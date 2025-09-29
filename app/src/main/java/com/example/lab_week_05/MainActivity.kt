package com.example.lab_week_05

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.lab_week_05.api.CatApiService
import com.example.lab_week_05.model.ImageData
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    private val apiResponseView: TextView by lazy {
        findViewById(R.id.api_response)
    }

    private val imageResultView: ImageView by lazy {
        findViewById(R.id.image_result)
    }

    private val imageLoader: ImageLoader by lazy {
        GlideLoader(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        val apiKey = "live_XJ33cqEFSI4eOO3jAtZ7XWwZ5pqjLQUW91c44K8rnDzDNUoQggUDF4eALgixBcT8"

        val call = catApiService.searchImages(apiKey, 1, "full") // ambil 1 gambar kucing
        call.enqueue(object : Callback<List<ImageData>> {
            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(MAIN_ACTIVITY, "Failed to get response", t)
                apiResponseView.text = "Error: ${t.message}"
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<ImageData>>, response: Response<List<ImageData>>) {
                if (response.isSuccessful) {
                    val images = response.body()
                    val firstImage = images?.firstOrNull()

                    // ✅ Ambil nama breed atau Unknown
                    val breedName = firstImage?.breeds?.firstOrNull()?.name ?: "Unknown"

                    // Tampilkan breed di TextView
                    apiResponseView.text = getString(R.string.breed_placeholder, breedName)

                    // ✅ Tampilkan gambar di ImageView
                    val imageUrl = firstImage?.imageUrl.orEmpty()
                    if (imageUrl.isNotBlank()) {
                        imageLoader.loadImage(imageUrl, imageResultView)
                    }
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
