package com.androrier.buttontoaction.network

import com.androrier.buttontoaction.model.MyAction
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: APICalls
):ApiHelper{
    override suspend fun getActions(): Response<List<MyAction>> = apiService.getConfig()

}