package com.designlife.justdo.setworkllm.domain.jdo

internal data class SetworkChat(
    val prompt : String = "",
    val tokens : String = "",
    val predict : Int = 1024
)