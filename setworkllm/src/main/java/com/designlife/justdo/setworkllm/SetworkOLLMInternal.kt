package com.designlife.justdo.setworkllm

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import com.designlife.justdo.setworkllm.common.utils.InternetHelper
import com.designlife.justdo.setworkllm.common.utils.PackageServiceLocator
import com.designlife.justdo.setworkllm.domain.usecase.OChatUseCase
import com.designlife.justdo.setworkllm.presentation.components.ChatFieldScreenViewComponent
import com.designlife.justdo.setworkllm.presentation.components.ChatFieldViewComponent
import com.designlife.justdo.setworkllm.presentation.viewmodel.OChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

internal class SetworkOLLMInternal(
    private val context: Context
) : SetworkOLLM {
    private var setworkMessage : SetworkOLLM.SetworkMessage? = null
    @Volatile private var isInitialized : Boolean = false
    @Volatile private lateinit var chatViewModel: OChatViewModel
    private lateinit var scope : CoroutineScope
    private lateinit var internetHelper: InternetHelper

    override fun init() {
        scope = CoroutineScope(Dispatchers.IO + Job())
        internetHelper = PackageServiceLocator.provideInternetHelper(context)
        this.setworkMessage
        try {
            if (!isInitialized){
                chatViewModel = PackageServiceLocator.provideOChatViewModel(context,internetHelper)
                isInitialized = true
            }
            if (internetHelper.isInternetAvailable()){
                scope.launch {
                    PackageServiceLocator.provideGithubRepository(context).fetchBaseUrl()
                }
                chatViewModel.initChatRepository(context)
            }else{
                internetHelper.observeInternet{ isAvailable ->
                    if (isAvailable){
                        scope.launch {
                            PackageServiceLocator.provideGithubRepository(context).fetchBaseUrl()
                            initStates()
                        }
                    }
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun protocol(setworkMessage: SetworkOLLM.SetworkMessage) {
        this.setworkMessage = setworkMessage
    }

    override fun clean() {
        super.clean()
        try {
            isInitialized = false
            if (::chatViewModel.isInitialized){
                chatViewModel.onClear()
            }
            PackageServiceLocator.clean()
            if (::scope.isInitialized){
                scope.coroutineContext.cancel()
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun initStates(){
        try {
            if (::chatViewModel.isInitialized){
                chatViewModel.initChatRepository(context)
            }
        }catch (e : Exception){
            e.printStackTrace()
        }

    }

    @Composable
    override fun ChatTextView() {
        if (internetHelper.isInternetAvailable()){
            initStates()
        }else{
            if (chatViewModel.isInternetAvailable.collectAsState().value){
                initStates()
            }
        }
        ChatFieldViewComponent(
            isInternetAvailable = chatViewModel.isInternetAvailable.collectAsState(),
            isThinking = chatViewModel.isStreaming.value,
            chatText = chatViewModel.chatPrompt.value,
            chatReplyText = chatViewModel.chatReplyText.value,
            onChatTextEvent = {chatViewModel.onEvent(OChatUseCase.OnChatEvent(it))},
            onChatStartEvent = {chatViewModel.onEvent(OChatUseCase.OnChatStartEvent)},
            onChatStopEvent = {
                chatViewModel.onEvent(OChatUseCase.OnChatStopEvent)
            },
            onChatAddEvent = {
                setworkMessage?.onChatRelay(chatViewModel.completeChatReply.value)
                chatViewModel.onEvent(OChatUseCase.OnChatAddEvent)
            },
            onBackPressEvent = {
            }
        )
    }

    @Composable
    override fun ChatScreenView() {
        if (internetHelper.isInternetAvailable()){
            initStates()
        }else{
            if (chatViewModel.isInternetAvailable.collectAsState().value){
                initStates()
            }
        }
        ChatFieldScreenViewComponent(
            isInternetAvailable = chatViewModel.isInternetAvailable.collectAsState(),
            isThinking = chatViewModel.isStreaming.value,
            chatText = chatViewModel.chatPrompt.value,
            chatReplyText = chatViewModel.chatReplyText.value,
            chatHistory = chatViewModel.chatHistory,
            onChatTextEvent = {chatViewModel.onEvent(OChatUseCase.OnChatEvent(it))},
            onChatStartEvent = {chatViewModel.onEvent(OChatUseCase.OnChatStartEvent)},
            onChatStopEvent = {
                chatViewModel.onEvent(OChatUseCase.OnChatStopEvent)
            },
            onBackPressEvent = {
            }
        )
    }
}