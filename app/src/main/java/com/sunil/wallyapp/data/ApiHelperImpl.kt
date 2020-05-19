package com.sunil.wallyapp.data

import kotlinx.coroutines.flow.flow

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override fun getUsersPhoto() = flow { emit(apiService.getUserPhoto()) }

}