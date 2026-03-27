package com.designlife.justdo.setworkllm.data.network.response

import com.google.gson.annotations.SerializedName


data class GithubURLResponse(
    @SerializedName("epoch") val createdAt : Long = 0L,
    @SerializedName("url") val url : String,
    @SerializedName("connector_id") val connectorId : String,
)