package com.designlife.justdo.setworkllm.common.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.designlife.justdo.setworkllm.common.constant.AppURLRouter
import com.designlife.justdo.setworkllm.data.network.NetworkBuilder
import com.designlife.justdo.setworkllm.data.network_service.GithubDynamicURLService
import com.designlife.justdo.setworkllm.data.network_service.OLLMService
import com.designlife.justdo.setworkllm.domain.repository.GithubRepository
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository
import com.designlife.justdo.setworkllm.presentation.viewmodel.OChatViewModel
import com.designlife.justdo.setworkllm.presentation.viewmodel.OChatViewModelFactory

internal object PackageServiceLocator {
    @SuppressLint("StaticFieldLeak")
    private var chatViewModel : OChatViewModel? = null
    private var chatRepository : OChatRepository? = null
    private var githubRepository: GithubRepository? = null


    fun provideChatRepository(context: Context) : OChatRepository {
        return createChatRepository(context)
    }

    internal fun createChatRepository(context: Context) : OChatRepository{
        val service = NetworkBuilder.instance(context, AppURLRouter.SETWORK_BASE_URL).create(OLLMService::class.java)
        chatRepository = OChatRepository(service)
        return chatRepository!!
    }



    fun provideGithubRepository(context: Context) : GithubRepository {
        return githubRepository?.let { it } ?: synchronized(context){
            githubRepository?.let { it } ?: createGithubRepository(context)
        }
    }

    internal fun createGithubRepository(context: Context) : GithubRepository{
        val service = NetworkBuilder.instance(context, AppURLRouter.GITHUB_BASE_URL).create(GithubDynamicURLService::class.java)
        githubRepository = GithubRepository(service)
        return githubRepository!!
    }


    fun provideOChatViewModel(context: Context) : OChatViewModel {
        return chatViewModel?.let { it } ?: synchronized(context){
            chatViewModel?.let { it } ?: createOChatViewModel(context)
        }
    }

    internal fun createOChatViewModel(context: Context) : OChatViewModel{
        val factor = OChatViewModelFactory(context)
        chatViewModel = ViewModelProvider(context as ViewModelStoreOwner,factor)[OChatViewModel::class.java]
        return chatViewModel!!
    }

    fun clean(){
        chatViewModel = null
        chatRepository = null
        githubRepository = null
    }
}