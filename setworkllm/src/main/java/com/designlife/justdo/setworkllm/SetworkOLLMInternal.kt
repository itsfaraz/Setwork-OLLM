package com.designlife.justdo.setworkllm

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import com.designlife.justdo.setworkllm.common.constant.AppURLRouter
import com.designlife.justdo.setworkllm.common.utils.PackageServiceLocator
import com.designlife.justdo.setworkllm.domain.usecase.OChatUseCase
import com.designlife.justdo.setworkllm.presentation.components.ChatFieldScreenViewComponent
import com.designlife.justdo.setworkllm.presentation.components.ChatFieldViewComponent
import com.designlife.justdo.setworkllm.presentation.viewmodel.OChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


internal class SetworkOLLMInternal(
    private val context: Context
) : SetworkOLLM {

    @Volatile private lateinit var chatViewModel: OChatViewModel
    private var scope = CoroutineScope(Dispatchers.IO + Job())
    override var chatResult: MutableState<String> = mutableStateOf("")


    override fun init() {
        scope.launch(Dispatchers.IO) {
            PackageServiceLocator
                .provideGithubRepository(context)
                .fetchBaseUrl()
            withContext(Dispatchers.Main){
                Log.i("FLOW", "init:  baseURL :${AppURLRouter.SETWORK_BASE_URL}")
                val chatRepository = PackageServiceLocator.provideChatRepository(context)
                chatViewModel = PackageServiceLocator.provideOChatViewModel(context,chatRepository)
            }
        }
    }

    override fun clean() {
        if (::chatViewModel.isInitialized){
            chatViewModel.onClear()
        }
        scope.cancel()
        chatResult.value = ""
        PackageServiceLocator.clean()
    }

    @Composable
    override fun ChatTextView() {
        ChatFieldViewComponent(
            isThinking = chatViewModel.isStreaming.value,
            chatText = chatViewModel.chatText.value,
            chatReplyText = chatViewModel.chatReplyText.value,
            onChatTextEvent = {chatViewModel.onEvent(OChatUseCase.OnChatEvent(it))},
            onSendEvent = {chatViewModel.onEvent(OChatUseCase.OnSendEvent)},
            onChatAddEvent = {
                chatResult.value = chatViewModel.completeChatReply.value
                chatViewModel.onEvent(OChatUseCase.OnChatAddEvent)
            }
        )
    }

    @Composable
    override fun ChatScreenView() {
        ChatFieldScreenViewComponent(
            isThinking = chatViewModel.isStreaming.value,
            chatText = chatViewModel.chatText.value,
            chatReplyText = chatViewModel.chatReplyText.value,
            chatHistory = chatViewModel.chatHistory,
            onChatTextEvent = {chatViewModel.onEvent(OChatUseCase.OnChatEvent(it))},
            onSendEvent = {chatViewModel.onEvent(OChatUseCase.OnSendEvent)},
        )
    }



}