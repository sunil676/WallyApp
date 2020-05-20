package com.sunil.wallyapp.data

import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Call

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override fun getUsersPhoto() = flow { emit(apiService.getUserPhoto()) }

}