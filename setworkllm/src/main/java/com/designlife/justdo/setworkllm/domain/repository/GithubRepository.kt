package com.designlife.justdo.setworkllm.domain.repository

import android.content.Context
import android.util.Log
import com.designlife.justdo.setworkllm.common.constant.AppURLRouter
import com.designlife.justdo.setworkllm.common.utils.PackageServiceLocator
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionRequest
import com.designlife.justdo.setworkllm.data.network.response.ChatSessionResponse
import com.designlife.justdo.setworkllm.data.network_service.GithubDynamicURLService
import com.designlife.justdo.setworkllm.data.network_service.OLLMService
import kotlinx.coroutines.flow.MutableStateFlow

class GithubRepository(
    private val githubService: GithubDynamicURLService,
) {
    suspend fun fetchBaseUrl() {
        try {
            val response = githubService.fetchBaseUrl()
            response?.let { if (it.isSuccessful) {
                it.body()?.let {
                    AppURLRouter.setAppURL(it.url)
                    Log.i("FLOW", "fetchBaseUrl: ${it.createdAt}, url :${it.url}, connector-id :${it.connectorId}")
                }
            } }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}