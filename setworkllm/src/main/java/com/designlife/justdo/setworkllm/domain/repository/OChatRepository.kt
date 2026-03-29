package com.designlife.justdo.setworkllm.domain.repository

import android.util.Log
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionEndRequest
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionRequest
import com.designlife.justdo.setworkllm.data.network_service.OLLMService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

internal class OChatRepository(
    private val chatService: OLLMService
) {
    var infiRun : Boolean = false
    var lineNO : Int = 1
    suspend fun onChatStream(request: ChatSessionRequest): Flow<String> = flow {
        infiRun = true
        while (infiRun){
            delay(100)
            emit("HELLO WORLD ---------------- ${lineNO}")
            lineNO +=1
        }


        return@flow

//        try {
//            val response = chatService.requestChatSession(request)
//            if (!response.isSuccessful) return@flow
//
//            val reader = response.body()
//                ?.byteStream()
//                ?.bufferedReader() ?: return@flow
//
//            while (true) {
//                val line = reader.readLine() ?: break
//
//                if (line.startsWith("data:")) {
//                    // Strip ALL "data:" prefixes (handles single and double)
//                    var json = line
//                    while (json.startsWith("data:")) {
//                        json = json.removePrefix("data:").trim()
//                    }
//
//                    if (json.isNotEmpty() && json != "[DONE]") {
//                        try {
//                            val obj = JSONObject(json)
//
//                            // Stop signal from llama.cpp
//                            if (obj.optBoolean("stop", false)) break
//
//                            val content = obj.optString("content")
//                            if (content.isNotEmpty()) {
//                                emit(content)
//                            }
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    suspend fun onChatExit(requestId : String) {
        try {
            infiRun = false
            lineNO = 1

//            val response = chatService.requestChatSessionKill(ChatSessionEndRequest(requestId = requestId))
//            response?.let {
//                if (it.isSuccessful) {
//                    it.body()?.let {
//                        Log.i("CHAT_REPOSITORY", "onChatExit: ${it.message}")
//                    }
//                }
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}