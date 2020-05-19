package com.sunil.wallyapp.data

import com.sunil.wallyapp.BuildConfig
import com.sunil.wallyapp.data.model.Photos
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("photos?client_id=${BuildConfig.API_KEY}")
    suspend fun getUserPhoto(): List<Photos>

    //https://api.unsplash.com/photos/?client_id=07c1N57iYmcIXVlhpvr1KsxuKE7aimcanuE8x6cXl8M
}