package com.designlife.justdo.setworkllm.domain.usecase

sealed class OChatUseCase {
    data class OnChatEvent(val text : String) : OChatUseCase()
    data object OnChatAddEvent : OChatUseCase()
    data object OnSendEvent : OChatUseCase()
}