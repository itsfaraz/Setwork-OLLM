package com.designlife.justdo.setworkllm.presentation.components

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.designlife.justdo.setworkllm.R
import com.designlife.justdo.setworkllm.ui.theme.ComponentColorPrimary
import com.designlife.justdo.setworkllm.ui.theme.NoInternetStyleFontSize
import com.designlife.justdo.setworkllm.ui.theme.chatReplyTextStyle
import com.designlife.justdo.setworkllm.ui.theme.chatViewDescriptionStyle
import com.designlife.justdo.setworkllm.ui.theme.chatViewHeaderStyle
import com.designlife.justdo.setworkllm.ui.theme.fontFamily

@Composable
internal fun ChatFieldViewComponent(
    color : Color = Color.White,
    isInternetAvailable : State<Boolean>,
    isThinking : Boolean,
    chatText : String,
    chatReplyText : String,
    onChatTextEvent : (text : String) -> Unit,
    onChatStartEvent : () -> Unit,
    onChatStopEvent : () -> Unit,
    onChatAddEvent : () -> Unit,
    onBackPressEvent : () -> Unit
) {
    val activity = LocalContext.current as Activity

    BackHandler(enabled = false) {
        onBackPressEvent()
        println("Back pressed!")
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(chatReplyText) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column {

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = color),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Good day !", style = chatViewHeaderStyle.value)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "How can, i help you today?", style = chatViewDescriptionStyle.value)
            }

            item{
                Spacer(modifier = Modifier.height(22.dp))
                ChatTextField(
                    networkState = isInternetAvailable.value,
                    isThinking = isThinking,
                    chatText = chatText,
                    onChatTextEvent = {
                        onChatTextEvent(it)
                    },
                    onChatStartEvent = {
                        onChatStartEvent()
                    },
                    onChatStopEvent = {
                        onChatStopEvent()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .heightIn(max = 160.dp) // max height constraint
                        .verticalScroll(scrollState) // makes content scrollable
                ) {
                    Text(modifier = Modifier.padding(horizontal = 4.dp), text = chatReplyText, style = chatReplyTextStyle.value, softWrap = true)

                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                if (chatReplyText.isNotEmpty()){
                    IconButton(onClick = {
                        onChatAddEvent()
                    }) {
                        Box(
                            modifier = Modifier.size(28.dp)
                                .background(color = Color.White, shape = CircleShape)
                                .border(width = 1.dp, color = ComponentColorPrimary, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ){
                            Icon(modifier = Modifier.size(14.dp), painter = painterResource(R.drawable.ic_plus), contentDescription = "Add Button", tint = ComponentColorPrimary)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

            }
        }

//        if (isInternetAvailable.value){
//
//        }else{
//            Box(
//                modifier = Modifier.fillMaxWidth().heightIn(max = 160.dp, min = 140.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(buildAnnotatedString {
//                    withStyle(style = SpanStyle(color = ComponentColorPrimary, fontFamily = fontFamily, fontSize = NoInternetStyleFontSize.value, fontWeight = FontWeight.SemiBold)){
//                        append("Internet")
//                    }
//                    append(" ")
//                    withStyle(style = SpanStyle(color = Color.Black, fontFamily = fontFamily, fontSize = NoInternetStyleFontSize.value, fontWeight = FontWeight.Normal)){
//                        append("Is Not Available")
//                    }
//                }, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
//            }
//        }
    }

}