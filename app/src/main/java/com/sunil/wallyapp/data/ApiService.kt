package com.sunil.wallyapp.data

import com.sunil.wallyapp.BuildConfig
import com.sunil.wallyapp.data.model.Photos
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface ApiService {

    @GET("photos?client_id=${BuildConfig.API_KEY}")
    suspend fun getUserPhoto(): List<Photos>

    @GET
    @Streaming
    fun downloadImage(@Url fileUrl: String?): Call<ResponseBody>

    //https://api.unsplash.com/photos/?client_id=07c1N57iYmcIXVlhpvr1KsxuKE7aimcanuE8x6cXl8M
}