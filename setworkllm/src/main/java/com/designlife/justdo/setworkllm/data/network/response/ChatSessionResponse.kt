package com.designlife.justdo.setworkllm.data.network.response

import com.google.gson.annotations.SerializedName

internal data class ChatSessionResponse(
    @SerializedName("index") val index : Int = 0,
    @SerializedName("content") val content : String = "",
    @SerializedName("stop") val isStopped : Boolean = false,
    @SerializedName("tokens_predicted") val serial : Int = 0
)