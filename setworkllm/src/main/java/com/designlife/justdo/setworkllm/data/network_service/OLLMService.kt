package com.designlife.justdo.setworkllm.data.network_service

import com.designlife.justdo.setworkllm.data.network.request.ChatSessionEndRequest
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionRequest
import com.designlife.justdo.setworkllm.data.network.response.ChatSessionKillResponse
import com.designlife.justdo.setworkllm.data.network.response.ChatSessionResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

internal interface OLLMService {
    @Streaming
    @POST("setwork/interact/start")
    suspend fun requestChatSession(@Body chatRequest: ChatSessionRequest): Response<ResponseBody>

    @POST("setwork/interact/stop")
    suspend fun requestChatSessionKill(@Body chatRequest: ChatSessionEndRequest): Response<ChatSessionKillResponse>
}