package com.designlife.justdo.setworkllm.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionRequest
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository
import com.designlife.justdo.setworkllm.domain.usecase.OChatUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class OChatViewModel(
    private val chatRepository : OChatRepository
) : ViewModel() {

    private val _chatId = mutableStateOf(0L)
    private val _isStreaming = mutableStateOf(false)
    val isStreaming = _isStreaming

    private val _chatText = mutableStateOf("")
    val chatText = _chatText

    private val _chatReplyText = mutableStateOf("")
    val chatReplyText = _chatReplyText

    private val _completeChatReply = mutableStateOf("")
    val completeChatReply = _completeChatReply


    private val _chatHistory= mutableStateListOf<String>()
    val chatHistory = _chatHistory



    fun onEvent(event : OChatUseCase){
        when(event){
            is OChatUseCase.OnChatEvent -> {
                _chatText.value = event.text
            }
            is OChatUseCase.OnChatStartEvent -> {
                onChatSessionStart()
            }
            is OChatUseCase.OnChatStopEvent -> {
                onChatSessionKill()
            }
            is OChatUseCase.OnChatAddEvent -> {
                _completeChatReply.value = ""
            }
        }
    }

    private fun onChatSessionStart() {
        viewModelScope.launch {
            _chatReplyText.value = ""
            if (_chatText.value.isEmpty()) return@launch

            _isStreaming.value = true
            _chatId.value = System.currentTimeMillis()

            val request = ChatSessionRequest(
                prompt = _chatText.value,
                streaming = true,
                nPredict = "2048",
                chatId = _chatId.value.toString()
            )
            _chatText.value = ""


            try {
                chatRepository.onChatStream(request)
                    .flowOn(Dispatchers.IO)
                    .collect { token ->
                        _chatReplyText.value += "$token "
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isStreaming.value = false
                _completeChatReply.value = _chatReplyText.value
                _chatReplyText.value = ""
            }
        }
    }

    private fun onChatSessionKill(){
        try {
            if (_chatReplyText.value.isNotEmpty()) {
                _chatHistory.add(_chatReplyText.value)
                _chatReplyText.value = ""
                Log.i("TEXT_FLOW", "onChatSessionStart: ${_chatHistory.get(_chatHistory.lastIndex).toString()}")
            }
            if (_chatId.value == 0L) return
            viewModelScope.launch {
                chatRepository.onChatExit(requestId = _chatId.value.toString())
                _isStreaming.value = false
                _chatId.value = 0
            }
        }catch (e : Exception){
            e.printStackTrace()
        }finally {
            _chatReplyText.value = ""
            _completeChatReply.value = ""
        }

    }

    fun onClear(){
        onChatSessionKill()
        _isStreaming.value = false
        _chatText.value = ""
        _chatReplyText.value = ""
        _completeChatReply.value = ""
        _chatHistory.clear()
        viewModelScope.cancel()
    }

}