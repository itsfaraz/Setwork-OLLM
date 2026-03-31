package com.designlife.justdo.setworkllm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import com.designlife.justdo.setworkllm.common.utils.InternetHelper
import com.designlife.justdo.setworkllm.common.utils.PackageServiceLocator
import com.designlife.justdo.setworkllm.common.utils.observeInternet
import com.designlife.justdo.setworkllm.domain.usecase.OChatUseCase
import com.designlife.justdo.setworkllm.presentation.components.ChatFieldScreenViewComponent
import com.designlife.justdo.setworkllm.presentation.components.ChatFieldViewComponent
import com.designlife.justdo.setworkllm.presentation.viewmodel.OChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SetworkOLLMInternal(
    private val context: Context
) : SetworkOLLM {

    @Volatile private var isInitialized : Boolean = false
    @Volatile private lateinit var chatViewModel: OChatViewModel
    private lateinit var scope : CoroutineScope
    override var chatResult: MutableState<String> = mutableStateOf("")

    override fun init() {
        scope = CoroutineScope(Dispatchers.IO + Job())
        try {
            if (!isInitialized){
                chatViewModel = PackageServiceLocator.provideOChatViewModel(context)
                isInitialized = true
            }
            if (InternetHelper.isInternetAvailable(context)){
                scope.launch {
                    PackageServiceLocator.provideGithubRepository(context).fetchBaseUrl()
                }
                chatViewModel.initChatRepository(context)
            }else{
                observeInternet(context){ isAvailable ->
                    if (isAvailable){
                        scope.launch {
                            PackageServiceLocator.provideGithubRepository(context).fetchBaseUrl()
                            initStates()
                        }
                    }else{
                        noInternet()
                    }
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
            Log.i("SetworkOLLM", "init: ${e.message}")
        }
    }

    private fun exit() {
        if (::chatViewModel.isInitialized){
            chatViewModel.onEvent(OChatUseCase.OnChatStopEvent)
        }
    }

    private fun noInternet(){
        if (::chatViewModel.isInitialized){
            chatViewModel.onEvent(OChatUseCase.OnChatStopEvent)
        }
    }


    override fun clean() {
        super.clean()
        try {
            isInitialized = false
            if (::chatViewModel.isInitialized){
                chatViewModel.onClear()
            }
            chatResult.value = ""
            PackageServiceLocator.clean()
        }catch (e : Exception){
            e.printStackTrace()
            Log.i("SetworkOLLM", "clean: ${e.message}")
        }
    }

    private fun initStates(){
        try {
            if (::chatViewModel.isInitialized){
                chatViewModel.initChatRepository(context)
            }
        }catch (e : Exception){
            Log.i("SetworkOLLM", "initStates: ${e.message}")
        }

    }

    @Composable
    override fun ChatTextView() {
        if (InternetHelper.isInternetAvailable(context)){
            initStates()
        }else{
            if (chatViewModel.isInternetAvailable.collectAsState().value){
                initStates()
            }else{
                noInternet()
            }
        }
        Log.i("SetworkOLLM", "SetworkOLLMInternal :: ChatTextView: init")
        ChatFieldViewComponent(
            isInternetAvailable = chatViewModel.isInternetAvailable.collectAsState(),
            isThinking = chatViewModel.isStreaming.value,
            chatText = chatViewModel.chatText.value,
            chatReplyText = chatViewModel.chatReplyText.value,
            onChatTextEvent = {chatViewModel.onEvent(OChatUseCase.OnChatEvent(it))},
            onChatStartEvent = {chatViewModel.onEvent(OChatUseCase.OnChatStartEvent)},
            onChatStopEvent = {
                chatViewModel.onEvent(OChatUseCase.OnChatStopEvent)
            },
            onChatAddEvent = {
                chatResult.value = chatViewModel.completeChatReply.value
                chatViewModel.onEvent(OChatUseCase.OnChatAddEvent)
            },
            onBackPressEvent = {
                exit()
            }
        )
    }

    @Composable
    override fun ChatScreenView() {
        if (InternetHelper.isInternetAvailable(context)){
            initStates()
        }else{
            if (chatViewModel.isInternetAvailable.collectAsState().value){
                initStates()
            }else{
                noInternet()
            }
        }
        Log.i("SetworkOLLM", "SetworkOLLMInternal :: ChatScreenView: init")
        ChatFieldScreenViewComponent(
            isInternetAvailable = chatViewModel.isInternetAvailable.collectAsState(),
            isThinking = chatViewModel.isStreaming.value,
            chatText = chatViewModel.chatText.value,
            chatReplyText = chatViewModel.chatReplyText.value,
            chatHistory = chatViewModel.chatHistory,
            onChatTextEvent = {chatViewModel.onEvent(OChatUseCase.OnChatEvent(it))},
            onChatStartEvent = {chatViewModel.onEvent(OChatUseCase.OnChatStartEvent)},
            onChatStopEvent = {
                chatViewModel.onEvent(OChatUseCase.OnChatStopEvent)
            },
            onBackPressEvent = {
                exit()
            }
        )
    }
}