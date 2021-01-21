package com.androrier.buttontoaction.network

import com.androrier.buttontoaction.model.MyAction
import retrofit2.Call
import retrofit2.Response

interface ApiHelper {
    suspend fun getActions(): Response<List<MyAction>>
}