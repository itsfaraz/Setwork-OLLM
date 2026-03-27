package com.designlife.justdo.setworkllm.data.network_service

import com.designlife.justdo.setworkllm.data.network.request.ChatSessionRequest
import com.designlife.justdo.setworkllm.data.network.response.ChatSessionKillResponse
import com.designlife.justdo.setworkllm.data.network.response.ChatSessionResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface OLLMService {
    @POST("completion")
    @Streaming
    suspend fun requestChatSession(@Body chatRequest: ChatSessionRequest): Response<ResponseBody>

    @POST("completion")
    suspend fun requestChatSessionKill(): Response<ChatSessionKillResponse>
}