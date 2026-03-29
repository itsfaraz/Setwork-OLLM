package com.designlife.justdo.setworkllm.domain.usecase

internal sealed class OChatUseCase {
    data class OnChatEvent(val text : String) : OChatUseCase()
    data object OnChatAddEvent : OChatUseCase()
    data object OnChatStartEvent : OChatUseCase()
    data object OnChatStopEvent : OChatUseCase()
}