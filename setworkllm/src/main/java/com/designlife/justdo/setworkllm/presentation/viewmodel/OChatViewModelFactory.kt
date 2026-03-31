package com.designlife.justdo.setworkllm.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository

internal class OChatViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OChatViewModel(context) as T
    }
}