package com.designlife.justdo.setworkllm.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.designlife.justdo.setworkllm.common.utils.InternetHelper
import com.designlife.justdo.setworkllm.common.utils.PackageServiceLocator
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionRequest
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository
import com.designlife.justdo.setworkllm.domain.usecase.OChatUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

internal class OChatViewModel(
    private val internetHelper: InternetHelper
) : ViewModel() {

    private val _isInternetAvailable = MutableStateFlow(internetHelper.isInternetAvailable())

    val isInternetAvailable : StateFlow<Boolean> = _isInternetAvailable.asStateFlow()
    private val _chatId = mutableStateOf(0L)
    private val _isStreaming = mutableStateOf(false)
    val isStreaming = _isStreaming

    private val _chatPrompt = mutableStateOf("")
    val chatPrompt = _chatPrompt

    private val _chatReplyText = mutableStateOf("")
    val chatReplyText = _chatReplyText

    private val _completeChatReply = mutableStateOf("")
    val completeChatReply = _completeChatReply


    private val _chatHistory= mutableStateListOf<String>()
    val chatHistory = _chatHistory

    private lateinit var _chatRepository : OChatRepository

    fun initChatRepository(context: Context){
        _chatRepository = PackageServiceLocator.provideChatRepository(context)
    }

    init {
        internetHelper.observeInternet{ isAvailable ->
            viewModelScope.launch(Dispatchers.Main.immediate) {
                _isInternetAvailable.value = isAvailable
            }
        }
    }

    fun onEvent(event : OChatUseCase){
        when(event){
            is OChatUseCase.OnChatEvent -> {
                _chatPrompt.value = event.text
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
        _chatId.value = System.currentTimeMillis() + _chatPrompt.value.hashCode()
        viewModelScope.launch(Dispatchers.IO) {
            _chatReplyText.value = ""
            if (_chatPrompt.value.isEmpty()) return@launch

            _isStreaming.value = true

            val request = ChatSessionRequest(
                prompt = _chatPrompt.value,
                streaming = true,
                nPredict = "2048",
                chatId = _chatId.value.toString()
            )
            _chatPrompt.value = ""

            try {
                if (::_chatRepository.isInitialized){
                    if (isInternetAvailable.value){
                       _chatRepository.onChatStream(request)
                            .flowOn(Dispatchers.IO)
                            .collect { token ->
                                if (_isStreaming.value){
                                    _chatReplyText.value += "$token "
                                }else{
                                    OChatRepository.streamState = false
                                    return@collect
                                }
                            }
                    }
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
            if (_completeChatReply.value.isNotEmpty()) {
                _chatHistory.add(_completeChatReply.value)
            }
            if (_chatId.value == 0L) return
            viewModelScope.launch(Dispatchers.IO) {
                if (isInternetAvailable.value){
                    _isStreaming.value = false
                    _chatRepository.onChatExit(requestId = _chatId.value.toString())
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }finally {
            _chatReplyText.value = ""
        }
    }

    fun onClear(){
        _isStreaming.value = false
        _chatPrompt.value = ""
        _chatReplyText.value = ""
        _completeChatReply.value = ""
        _chatId.value = 0L
        _chatHistory.clear()
        viewModelScope.cancel()
    }

}