package com.designlife.justdo.setworkllm.data.network.request

import com.google.gson.annotations.SerializedName

internal data class ChatSessionRequest(
    @SerializedName("prompt") val prompt : String,
    @SerializedName("streaming") val streaming : Boolean,
    @SerializedName("npredict") val nPredict : String,
    @SerializedName("requestId") val chatId : String,
)