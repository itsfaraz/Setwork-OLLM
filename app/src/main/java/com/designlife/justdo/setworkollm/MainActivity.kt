package com.designlife.justdo.setworkollm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.designlife.justdo.setworkllm.SetworkOLLM

class MainActivity : ComponentActivity(), SetworkOLLM.SetworkMessage {
    override fun onCreate(savedInstanceState: Bundle?) {
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
                        SetworkOLLM.chatSDK(this@MainActivity)
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green)) {
                        Text("Start")
                    }
                    Button(onClick = {
                        SetworkOLLM.destroy()
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("Stop")
                    }
                }
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
                    SetworkOLLM.ChatTextView()
                }

                if (screenViewToggle.value){
                    SetworkOLLM.ChatScreenView()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onChatRelay(message: String) {
        Log.i("FLOW", "onChatRelay: ${message}")
    }
}
