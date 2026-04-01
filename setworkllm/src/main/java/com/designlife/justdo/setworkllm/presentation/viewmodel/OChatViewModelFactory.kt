package com.designlife.justdo.setworkllm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.designlife.justdo.setworkllm.common.utils.InternetHelper

internal class OChatViewModelFactory(
    private val internetHelper: InternetHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OChatViewModel(internetHelper) as T
    }
}