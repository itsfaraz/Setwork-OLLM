package com.designlife.justdo.setworkllm.data.network.response

import com.google.gson.annotations.SerializedName

internal data class ChatSessionKillResponse(
    @SerializedName("exit") val exit : Int = 0,
    @SerializedName("message") val message : String = "",
    @SerializedName("last_time") val sessionTime : Long = 0L,
)