package com.designlife.justdo.setworkllm.domain.repository

import android.util.Log
import com.designlife.justdo.setworkllm.data.network.NetworkBuilder.clearNetwork
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionEndRequest
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionRequest
import com.designlife.justdo.setworkllm.data.network_service.OLLMService
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeout
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.BufferedReader
import kotlin.concurrent.Volatile

internal class OChatRepository(
    private val chatService: OLLMService
) {
    suspend fun onChatStream(request: ChatSessionRequest): Flow<String> = flow {
        streamState = true
        return@flow try {
            if (!streamState) return@flow
            val response = chatService.requestChatSession(request)
            if (!response.isSuccessful) return@flow
            val reader = response.body()
                ?.byteStream()
                ?.bufferedReader() ?: return@flow

            while (streamState) {
                val line = reader.readLine() ?: break
                if (!streamState) {
                   break
                }
                if (line.startsWith("data:")) {
                    // Strip ALL "data:" prefixes (handles single and double)
                    var json = line
                    while (json.startsWith("data:")) {
                        json = json.removePrefix("data:").trim()
                    }
                    if (json.isNotEmpty() && json != "[DONE]") {
                        try {
                            val obj = JSONObject(json)
                            // Stop signal from llama.cpp
                            if (obj.optBoolean("stop", false)) break
                            val content = obj.optString("content")
                            if (content.isNotEmpty()) {
                                emit(content)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun onChatExit(requestId : String) {
        try {
            streamState = false
            val response = chatService.requestChatSessionKill(ChatSessionEndRequest(requestId = requestId))
            response.let {
                if (it.isSuccessful) {
                    it.body()?.let {}
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object{
        @Volatile internal var streamState : Boolean = false
    }
}