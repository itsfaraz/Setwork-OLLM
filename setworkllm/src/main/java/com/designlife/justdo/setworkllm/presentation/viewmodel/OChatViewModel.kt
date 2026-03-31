package com.designlife.justdo.setworkllm.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.designlife.justdo.setworkllm.common.constant.AppURLRouter
import com.designlife.justdo.setworkllm.common.utils.InternetHelper
import com.designlife.justdo.setworkllm.common.utils.PackageServiceLocator
import com.designlife.justdo.setworkllm.common.utils.observeInternet
import com.designlife.justdo.setworkllm.data.network.NetworkBuilder
import com.designlife.justdo.setworkllm.data.network.request.ChatSessionRequest
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository
import com.designlife.justdo.setworkllm.domain.usecase.OChatUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

internal class OChatViewModel(
    @SuppressLint("StaticFieldLeak") private val context: Context
) : ViewModel() {

    private val _isInternetAvailable = MutableStateFlow(InternetHelper.isInternetAvailable(context))

    val isInternetAvailable : StateFlow<Boolean> = _isInternetAvailable.asStateFlow()
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

    private lateinit var _chatRepository : OChatRepository

    fun initChatRepository(context: Context){
        _chatRepository = PackageServiceLocator.provideChatRepository(context)
    }

    init {
        observeInternet(context){ isAvailable ->
            println("🔥 CALLBACK HIT: $isAvailable")
            viewModelScope.launch(Dispatchers.Main.immediate) {
                _isInternetAvailable.value = isAvailable
            }
        }
    }

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
        Log.i("Session_Flow", "onChatSessionStart: ")
        viewModelScope.launch {
            _chatReplyText.value = ""
            Log.i("Session_Flow", "onChatSessionStart: in viewmodel scope ")
            if (_chatText.value.isEmpty()) return@launch

            _isStreaming.value = true
            _chatId.value = System.currentTimeMillis() + _chatText.value.hashCode()
            Log.i("Session_Flow", "onChatSessionStart: in chat Id : ${_chatId.value} ")

            val request = ChatSessionRequest(
                prompt = _chatText.value,
                streaming = true,
                nPredict = "2048",
                chatId = _chatId.value.toString()
            )
            Log.i("Session_Flow", "onChatSessionStart: in chat Id : ${_chatText.value} ")
            _chatText.value = ""


            try {
                if (::_chatRepository.isInitialized){
                    Log.i("Session_Flow", "onChatSessionStart: in chat Id : ${isInternetAvailable.value} ")
                    if (isInternetAvailable.value){
                        Log.i("Session_Flow", "onChatSessionStart: Before api call on ${AppURLRouter.SETWORK_BASE_URL}")
                       _chatRepository.onChatStream(request)
                            .flowOn(Dispatchers.IO)
                            .collect { token ->
                                Log.i("Session_Flow", "onChatSessionStart: onChatStream collect :: ${token}")
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
            if (_chatReplyText.value.isNotEmpty()) {
                _chatHistory.add(_chatReplyText.value)
                _isStreaming.value = false
                _chatReplyText.value = ""
                Log.i("EXIT_FLOW", "onChatSessionKill: ${_chatHistory.get(_chatHistory.lastIndex).toString()}")
            }
            if (_chatId.value == 0L) return
            viewModelScope.launch {
                try {
                    Log.i("EXIT_FLOW", "onChatSessionKill: chat repository :: isInitialized ${::_chatRepository.isInitialized}")
                    if (::_chatRepository.isInitialized){
                        if (isInternetAvailable.value){
                            Log.i("EXIT_FLOW", "onChatSessionKill: onChatExit")
                            _chatRepository.onChatExit(requestId = _chatId.value.toString())
                        }
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                }finally {
                    _chatId.value = 0
                    viewModelScope.cancel()
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }finally {
            _chatReplyText.value = ""
            _completeChatReply.value = ""
        }
    }

    fun onClear(){
        _isStreaming.value = false
        _chatText.value = ""
        _chatReplyText.value = ""
        _completeChatReply.value = ""
        _chatHistory.clear()
        viewModelScope.cancel()
    }

}