package com.designlife.justdo.setworkllm.data.network_service

import com.designlife.justdo.setworkllm.data.network.response.GithubURLResponse
import retrofit2.Response
import retrofit2.http.GET

internal interface GithubDynamicURLService {
    @GET("download/1.0.0/setwork_metadata.json")
    suspend fun fetchBaseUrl() : Response<GithubURLResponse>
}