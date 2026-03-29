package com.designlife.justdo.setworkllm.data.network.request

import com.google.gson.annotations.SerializedName

internal data class ChatSessionEndRequest(
    @SerializedName("requestId") val requestId : String,
)