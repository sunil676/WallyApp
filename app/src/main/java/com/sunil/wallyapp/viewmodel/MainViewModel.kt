package com.sunil.wallyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunil.wallyapp.data.ApiHelper
import com.sunil.wallyapp.data.model.Photos
import com.sunil.wallyapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val apiHelper: ApiHelper) : ViewModel() {

    private val usersPhoto = MutableLiveData<Resource<List<Photos>>>()

    init {
        fetchUsersPhoto()
    }

    private fun fetchUsersPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            usersPhoto.postValue(Resource.loading(null))
            apiHelper.getUsersPhoto()
                .catch { e ->
                    usersPhoto.postValue(Resource.error(e.toString(), null))
                }
                .collect {
                    usersPhoto.postValue(Resource.success(it))
                }
        }
    }

    fun getUsersPhoto(): LiveData<Resource<List<Photos>>> {
        return usersPhoto
    }

}