package com.androrier.buttontoaction.network

import com.androrier.buttontoaction.model.MyAction
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface APICalls {
    @GET("/api/config")
    suspend fun getConfig(): Response<List<MyAction>>

}