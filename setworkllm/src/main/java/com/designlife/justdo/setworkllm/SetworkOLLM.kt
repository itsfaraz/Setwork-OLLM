package com.designlife.justdo.setworkllm

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.designlife.justdo.setworkllm.presentation.components.ChatNotAvailableComponent

abstract class SetworkOLLM{
    internal abstract fun init()
    abstract fun protocol(setworkMessage: SetworkMessage)

    internal abstract fun clean()

    internal @Composable abstract fun ChatTextView()
    internal @Composable abstract fun ChatScreenView()

    interface SetworkMessage {
        fun onChatRelay(message : String)
    }
    companion object{
        internal val sdkCheck = mutableStateOf(false)
        @Volatile internal var instance : SetworkOLLM? = null
        private val lock = Any()

        fun chatSDK(context: Context): SetworkOLLM {
            return instance ?: synchronized(lock) {
                instance ?: createInstance(context.applicationContext).also {
                    sdkCheck.value = true
                    instance = it
                    instance?.init()
                }
            }
        }

        fun destroy() {
            synchronized(lock) {
                instance?.clean()
                instance = null
                sdkCheck.value = false
            }
        }

        private fun createInstance(context: Context): SetworkOLLM {
            return SetworkOLLMInternal(context)
        }

        @Composable fun ChatTextView() {
            if (sdkCheck.value){
                instance?.ChatTextView()
            }else {
                ChatNotAvailableComponent("Setwork chat window is not available")
            }
        }

        @Composable fun ChatScreenView() {
            if (sdkCheck.value){
                instance?.ChatScreenView()
            }else {
                ChatNotAvailableComponent("Setwork chat screen is not available")
            }
        }
    }
}