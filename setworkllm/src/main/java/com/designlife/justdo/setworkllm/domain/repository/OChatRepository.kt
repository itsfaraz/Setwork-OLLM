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

    private var activeReader: BufferedReader? = null
    private var activeBody: ResponseBody? = null

    suspend fun onChatStream(request: ChatSessionRequest): Flow<String> = flow {
        streamState = true
        Log.i("Session_Flow", "OChatRepository: onChatStream api init ")
        return@flow try {
            if (!streamState) return@flow
            Log.i("Session_Flow", "OChatRepository: onChatStream api request ")
            val response = chatService.requestChatSession(request)
            if (!response.isSuccessful) return@flow

            Log.i("Session_Flow", "OChatRepository: onChatStream api request successfull ")

            val reader = response.body()
                ?.byteStream()
                ?.bufferedReader() ?: return@flow
            activeBody = response.body()
            activeReader = reader

            while (currentCoroutineContext().isActive && streamState) {
                val line = reader.readLine() ?: break
                Log.i("Session_Flow", "OChatRepository: onChatStream data : ${line} ")
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
        } finally {
            activeReader?.close()
            activeReader = null
            activeBody?.close()
        }
    }

    suspend fun onChatExit(requestId : String) {
        try {
            Log.i("EXIT_FLOW", "onChatExit: init")
            streamState = false
            Log.i("EXIT_FLOW", "onChatExit: requestChatSessionKill")
            val response = chatService.requestChatSessionKill(ChatSessionEndRequest(requestId = requestId))
            response?.let {
                if (it.isSuccessful) {
                    it.body()?.let {
                        Log.i("EXIT_FLOW", "onChatExit: response :: success")
                        clearNetwork()
                        Log.i("EXIT_FLOW", "onChatExit: response :: clearNetwork")
                        Log.i("EXIT_FLOW", "onChatExit: response :: cancel network")
                    }
                }
            }
        } catch (e : TimeoutCancellationException){
            Log.i("EXIT_FLOW", "onChatExit: TimeoutCancellationException : ${e.message}")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun forceStopStream() {
        streamState = false
        try {
            activeReader?.close()
            activeReader = null
            activeBody?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object{
        @Volatile internal var streamState : Boolean = false
    }
}