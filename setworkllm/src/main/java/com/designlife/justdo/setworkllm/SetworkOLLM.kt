package com.designlife.justdo.setworkllm

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

public interface SetworkOLLM{
    var chatResult : MutableState<String>
    public fun init()
    public fun clean()

    public @Composable fun ChatTextView()
    public @Composable fun ChatScreenView()
    companion object{
        @Volatile private var instance : SetworkOLLM? = null

        fun chatSDK(context: Context) : SetworkOLLM{
            return instance?.let { it } ?: synchronized(context){
                instance?.let { it } ?: createInstance(context)
            }
        }

        private fun createInstance(context: Context): SetworkOLLM {
            instance = SetworkOLLMInternal(context)
            return instance!!
        }
    }
}