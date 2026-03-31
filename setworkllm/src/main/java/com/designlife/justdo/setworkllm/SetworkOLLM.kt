package com.designlife.justdo.setworkllm

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.designlife.justdo.setworkllm.domain.repository.OChatRepository

public interface SetworkOLLM{
    var chatResult : MutableState<String>
    public fun init()

    public fun clean(){
        try {
            clear()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    public @Composable fun ChatTextView()
    public @Composable fun ChatScreenView()
    companion object{
        @Volatile internal var instance : SetworkOLLM? = null

        fun chatSDK(context: Context) : SetworkOLLM{
            return instance?.let { it } ?: synchronized(context){
                instance?.let { it } ?: createInstance(context)
            }
        }

        private fun clear(){
            OChatRepository.streamState = false
            instance = null
        }

        private fun createInstance(context: Context): SetworkOLLM {
            instance = SetworkOLLMInternal(context)
            return instance!!
        }
    }
}