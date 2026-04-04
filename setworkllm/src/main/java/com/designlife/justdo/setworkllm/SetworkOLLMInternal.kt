package com.designlife.justdo.setworkllm

import android.content.Context
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.designlife.justdo.setworkllm.common.utils.InternetHelper
import com.designlife.justdo.setworkllm.common.utils.PackageServiceLocator
import com.designlife.justdo.setworkllm.data.network.NetworkBuilder
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository
import com.designlife.justdo.setworkllm.domain.usecase.OChatUseCase
import com.designlife.justdo.setworkllm.presentation.components.ChatFieldScreenViewComponent
import com.designlife.justdo.setworkllm.presentation.components.ChatFieldViewComponent
import com.designlife.justdo.setworkllm.presentation.viewprovider.OChatViewProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren

internal class SetworkOLLMInternal(
    private val context: Context
) : SetworkOLLM() {
    private val TAG = this@SetworkOLLMInternal::class.java.simpleName
    private var setworkMessage: SetworkOLLM.SetworkMessage? = null
    private lateinit var internetHelper: InternetHelper
    @Volatile
    private var isInitialized: Boolean = false
    @Volatile
    private lateinit var chatViewProvider: OChatViewProvider
    private lateinit var scope: CoroutineScope

    @Synchronized
    override fun init() {
        scope = PackageServiceLocator.provideScope()
        internetHelper = PackageServiceLocator.provideInternetHelper(context)
        scope?.let { scope ->
            try {
                if (!isInitialized) {
                    chatViewProvider = PackageServiceLocator
                        .provideOChatViewProvider(scope,internetHelper)
                    isInitialized = true
                }
                if (internetHelper.isInternetAvailable()) {
                    chatViewProvider.initChatRepository()
                }
            } catch (e : CancellationException){

            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                Log.i(TAG, "init: sdk initialized")
            }
        }
    }


    override fun protocol(setworkMessage: SetworkOLLM.SetworkMessage) {
        this.setworkMessage = setworkMessage
    }

    @Synchronized
    override fun clean() {
        try {
            isInitialized = false
            if (::scope.isInitialized) {
                scope.cancel()
            }
            OChatRepository.streamState = false
            if (::chatViewProvider.isInitialized) {
                chatViewProvider.onClear()
            }
            NetworkBuilder.clearNetwork()
            PackageServiceLocator.clean()
        } catch (e : CancellationException){
        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            Log.i(TAG, "clean: sdk clean")
        }
    }

    @Composable
    override fun ChatTextView() {
        if (::chatViewProvider.isInitialized){
            ChatFieldViewComponent(
                isInternetAvailable = chatViewProvider.isInternetAvailable.collectAsState(),
                isThinking = chatViewProvider.isStreaming.value,
                chatText = chatViewProvider.chatPrompt.value,
                chatReplyText = chatViewProvider.chatReplyText.value,
                onChatTextEvent = { chatViewProvider.onEvent(OChatUseCase.OnChatEvent(it)) },
                onChatStartEvent = { chatViewProvider.onEvent(OChatUseCase.OnChatStartEvent) },
                onChatStopEvent = {
                    chatViewProvider.onEvent(OChatUseCase.OnChatStopEvent)
                },
                onChatAddEvent = {
                    setworkMessage?.onChatRelay(chatViewProvider.completeChatReply.value)
                    chatViewProvider.onEvent(OChatUseCase.OnChatAddEvent)
                },
                onBackPressEvent = {
                }
            )
        }
    }

    @Composable
    override fun ChatScreenView() {
        if (::chatViewProvider.isInitialized){
            ChatFieldScreenViewComponent(
                isInternetAvailable = chatViewProvider.isInternetAvailable.collectAsState(),
                isThinking = chatViewProvider.isStreaming.value,
                chatText = chatViewProvider.chatPrompt.value,
                chatReplyText = chatViewProvider.chatReplyText.value,
                chatHistory = chatViewProvider.chatHistory,
                onChatTextEvent = { chatViewProvider.onEvent(OChatUseCase.OnChatEvent(it)) },
                onChatStartEvent = { chatViewProvider.onEvent(OChatUseCase.OnChatStartEvent) },
                onChatStopEvent = {
                    chatViewProvider.onEvent(OChatUseCase.OnChatStopEvent)
                },
                onBackPressEvent = {
                }
            )
        }
    }
}