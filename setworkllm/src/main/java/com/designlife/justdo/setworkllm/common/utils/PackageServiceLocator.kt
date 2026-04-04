package com.designlife.justdo.setworkllm.common.utils

import android.content.Context
import com.designlife.justdo.setworkllm.common.constant.AppURLRouter
import com.designlife.justdo.setworkllm.data.network.NetworkBuilder
import com.designlife.justdo.setworkllm.data.network_service.OLLMService
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository
import com.designlife.justdo.setworkllm.presentation.viewprovider.OChatViewProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren

internal object PackageServiceLocator {
    private var chatViewProvider: OChatViewProvider? = null
    private var chatRepository: OChatRepository? = null
    private var internetHelper: InternetHelper? = null
    private var scope: CoroutineScope? = null
    private var chatJob : Job? = null

    private var lock = Any()


    internal fun provideInternetHelper(context: Context): InternetHelper {
        return internetHelper?.let { it } ?: synchronized(context) {
            internetHelper?.let { it } ?: createInternetHelper(context)
        }
    }

    private fun createInternetHelper(context: Context): InternetHelper {
        internetHelper = InternetHelper(context)
        return internetHelper!!
    }

    fun provideChatRepository(): OChatRepository {
        return createChatRepository()
    }

    internal fun createChatRepository(): OChatRepository {
        val service =
            NetworkBuilder.instance(AppURLRouter.SETWORK_BASE_URL).create(OLLMService::class.java)
        chatRepository = OChatRepository(service)
        return chatRepository!!
    }

    fun provideOChatViewProvider(scope: CoroutineScope,internetHelper: InternetHelper): OChatViewProvider {
        return chatViewProvider?.let { it } ?: synchronized(lock) {
            chatViewProvider?.let { it } ?: createOChatProvider(scope,internetHelper)
        }
    }

    internal fun createOChatProvider(scope: CoroutineScope,internetHelper: InternetHelper): OChatViewProvider {
        chatViewProvider = OChatViewProvider(
            scope = scope,
            internetHelper = internetHelper
        )
        return chatViewProvider!!
    }

    internal fun provideScope() : CoroutineScope{
        return scope?.let { it } ?: synchronized(lock){
            scope?.let { it } ?: createScope()
        }
    }

    private fun createScope(): CoroutineScope {
        chatJob = SupervisorJob()
        scope =  CoroutineScope(Dispatchers.IO + chatJob!!)
        return scope!!
    }

    fun clean() {
        chatViewProvider = null
        chatRepository = null
        internetHelper = null
        chatJob?.cancelChildren()
        chatJob?.cancel()
        scope?.cancel()
        chatJob = null
        scope = null

    }
}