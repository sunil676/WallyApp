package com.sunil.wallyapp.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sunil.wallyapp.data.ApiHelper
import com.sunil.wallyapp.data.model.Photos
import com.sunil.wallyapp.utils.Resource
import com.sunil.wallyapp.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.lang.IllegalStateException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TestMainViewModel {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var apiHelper: ApiHelper

    @Mock
    private lateinit var apiPhotoObserver: Observer<Resource<List<Photos>>>

    @Before
    fun setUp() {
        // do something if required
    }

    @Test
    fun apiSuccessResponse() {
        testCoroutineRule.runBlockingTest {
            Mockito.doReturn(flowOf(emptyList<Photos>()))
                .`when`(apiHelper)
                .getUsersPhoto()
            val viewModel = MainViewModel(apiHelper)
            viewModel.getUsersPhoto().observeForever(apiPhotoObserver)
            Mockito.verify(apiHelper).getUsersPhoto()
            Mockito.verify(apiPhotoObserver).onChanged(Resource.success(emptyList()))
            viewModel.getUsersPhoto().removeObserver(apiPhotoObserver)
        }

    }

    @Test
    fun testApiError(){
        testCoroutineRule.runBlockingTest {
            val errorMessage = "API error"
            Mockito.doReturn(flow<List<Photos>>{
                throw IllegalStateException(errorMessage)
            })
                .`when`(apiHelper)
                .getUsersPhoto()
            val viewModel = MainViewModel(apiHelper)
            viewModel.getUsersPhoto().observeForever(apiPhotoObserver)
            Mockito.verify(apiHelper).getUsersPhoto()
            Mockito.verify(apiPhotoObserver).onChanged(Resource.error(IllegalStateException(errorMessage).toString(), null))
            viewModel.getUsersPhoto().removeObserver(apiPhotoObserver)
        }
    }

    @After
    fun tearDown() {
        // do something if required
    }

}