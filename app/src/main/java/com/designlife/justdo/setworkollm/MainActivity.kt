package com.designlife.justdo.setworkollm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.designlife.justdo.setworkllm.SetworkOLLM
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var setworkChat : SetworkOLLM
    override fun onCreate(savedInstanceState: Bundle?) {
        setworkChat = SetworkOLLM.chatSDK(this)
        setworkChat.init()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val widgetViewToggle = remember {
                mutableStateOf(false)
            }
            val screenViewToggle = remember {
                mutableStateOf(false)
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        screenViewToggle.value = false
                        widgetViewToggle.value = true
                    }) { Text("Widget View") }
                    Button(onClick = {
                        screenViewToggle.value = true
                        widgetViewToggle.value = false
                    }) { Text("Screen View") }
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (widgetViewToggle.value){
                    setworkChat.ChatTextView()
                }

                if (screenViewToggle.value){
                    setworkChat.ChatScreenView()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        setworkChat.clean()
    }

    override fun onDestroy() {
        super.onDestroy()
        setworkChat.clean()
    }
}
