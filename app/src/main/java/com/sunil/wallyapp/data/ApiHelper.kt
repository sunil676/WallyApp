package com.sunil.wallyapp.data

import com.sunil.wallyapp.data.model.Photos
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call

interface ApiHelper {

    fun getUsersPhoto(): Flow<List<Photos>>

}