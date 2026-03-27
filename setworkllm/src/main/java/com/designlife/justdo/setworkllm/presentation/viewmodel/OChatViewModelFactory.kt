package com.designlife.justdo.setworkllm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository

internal class OChatViewModelFactory(
    private val chatRepository : OChatRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OChatViewModel(chatRepository) as T
    }
}