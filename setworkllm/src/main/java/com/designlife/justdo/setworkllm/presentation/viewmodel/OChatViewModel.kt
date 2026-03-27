package com.designlife.justdo.setworkllm.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionRequest
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository
import com.designlife.justdo.setworkllm.domain.usecase.OChatUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class OChatViewModel(
    private val chatRepository : OChatRepository
) : ViewModel() {

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
            is OChatUseCase.OnSendEvent -> {
                onChatSendEvent()
            }
            is OChatUseCase.OnChatAddEvent -> {
               //
            }
        }
    }

    private fun onChatSendEvent() {
        if (_completeChatReply.value.isNotEmpty()){
            _chatHistory.add(_completeChatReply.value)
            _completeChatReply.value = ""
        }
        if (_chatText.value.isNotEmpty()) {
            _isStreaming.value = true
            _chatReplyText.value = ""
            val request = ChatSessionRequest(prompt = _chatText.value, streaming = true, nPredict = "2048")
            _chatText.value = ""
            val bufferReader = StringBuilder()
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.onChatStream(request).collectLatest { token ->
                    withContext(Dispatchers.Main.immediate) {
                        bufferReader.append(token)
                        _chatReplyText.value = bufferReader.toString()
                    }
                }
                withContext(Dispatchers.Main.immediate){
                    _isStreaming.value = false
                    _completeChatReply.value = _chatReplyText.value
                }
            }
        }
    }


    fun onClear(){
        _isStreaming.value = false
        _chatText.value = ""
        _chatReplyText.value = ""
        _completeChatReply.value = ""
        viewModelScope.cancel()
    }

}