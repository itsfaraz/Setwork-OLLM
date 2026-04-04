package com.designlife.justdo.setworkllm.common.constant

import kotlin.concurrent.Volatile

internal object AppURLRouter {
    internal const val GITHUB_BASE_URL = "https://github.com/itsfaraz/orange-bytes-server/releases/"
    @Volatile internal var SETWORK_BASE_URL = "https://orange-bytes-network.duckdns.org"

    fun setAppURL(generatedURL : String){
        this.SETWORK_BASE_URL = generatedURL
    }
}