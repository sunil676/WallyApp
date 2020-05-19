package com.sunil.wallyapp.data

import com.sunil.wallyapp.data.model.Photos
import kotlinx.coroutines.flow.Flow

interface ApiHelper {

    fun getUsersPhoto(): Flow<List<Photos>>
}