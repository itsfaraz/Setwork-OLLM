package com.designlife.justdo.setworkllm.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.designlife.justdo.setworkllm.R
import com.designlife.justdo.setworkllm.ui.theme.ChatBackgroundColorActiveLight
import com.designlife.justdo.setworkllm.ui.theme.ChatBackgroundColorLight
import com.designlife.justdo.setworkllm.ui.theme.ComponentColorPrimary
import com.designlife.justdo.setworkllm.ui.theme.chatReplyTextStyle
import com.designlife.justdo.setworkllm.ui.theme.chatViewDescriptionStyle
import com.designlife.justdo.setworkllm.ui.theme.chatViewHeaderStyle

@Composable
fun ChatFieldScreenViewComponent(
    color : Color = Color.White,
    isThinking : Boolean,
    chatText : String,
    chatReplyText : String,
    chatHistory : List<String>,
    onChatTextEvent : (text : String) -> Unit,
    onSendEvent : () -> Unit
) {

    val scrollState = rememberScrollState()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(chatReplyText) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    LaunchedEffect(chatHistory.size) {
        lazyListState.animateScrollToItem(chatHistory.size)
    }

    var selectedIndex by remember{
        mutableStateOf(-1)
    }

    var layoutToggle by remember {
        mutableStateOf(false)
    }

    AnimatedVisibility(!layoutToggle) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(.5F).background(color = color),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(22.dp))
                Text(text = "Good day !", style = chatViewHeaderStyle.value)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "How can, i help you today?", style = chatViewDescriptionStyle.value)
                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = color),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    state = lazyListState
                ) {
                    itemsIndexed(items = chatHistory){ index, chat ->
                        if (chat.isNotEmpty()){
                            Row(modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 12.dp, vertical = 2.dp)
                                .background(color = ChatBackgroundColorLight, shape = RoundedCornerShape(12.dp)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(.9F)
                                        .padding(horizontal = 4.dp)
                                        .wrapContentHeight() // max height constraint
                                        .padding(vertical = 2.dp)
                                    // makes content scrollable
                                ) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(modifier = Modifier
                                        .wrapContentSize()
                                        .padding(horizontal = 2.dp, vertical = 6.dp),
                                        text = chat,
                                        style = chatReplyTextStyle.value,
                                        softWrap = true, maxLines = 3)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                IconButton(onClick = {
                                    selectedIndex = index
                                    layoutToggle = true
                                }) {
                                    Box(
                                        modifier = Modifier.size(24.dp)
                                            .background(color = ChatBackgroundColorLight, shape = CircleShape)
                                            .border(width = 1.dp, color = ComponentColorPrimary, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Icon(modifier = Modifier.size(10.dp), painter = painterResource(R.drawable.ic_open_out), contentDescription = "Send", tint = ComponentColorPrimary)
                                    }
                                }

                            }

                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth().background(color = color),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxHeight(.48F) // max height constraint
                        .verticalScroll(scrollState) // makes content scrollable
                ) {
                    Text(modifier = Modifier.wrapContentSize().padding(horizontal = 2.dp, vertical = 4.dp), text = chatReplyText, style = chatReplyTextStyle.value, softWrap = true)

                }
                Spacer(modifier = Modifier.height(16.dp))
                ChatTextField(
                    isThinking = isThinking,
                    chatText = chatText,
                    onChatTextEvent = {
                        onChatTextEvent(it)
                    },
                    onChatButtonEvent = {
                        onSendEvent()
                    }
                )
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }


    AnimatedVisibility(layoutToggle) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Column(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
                Spacer(modifier = Modifier.height(38.dp))
                Text(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 2.dp, vertical = 6.dp)
                    .background(color = Color.White),
                    text = chatHistory.get(selectedIndex),
                    style = chatReplyTextStyle.value,
                    softWrap = true, maxLines = 3)
                Spacer(modifier = Modifier.height(8.dp))
            }


            IconButton(onClick = {
                layoutToggle = false
            }) {
                Box(
                    modifier = Modifier.size(34.dp)
                        .background(color = ChatBackgroundColorLight, shape = CircleShape)
                        .border(width = 1.dp, color = ComponentColorPrimary, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(modifier = Modifier.size(16.dp), painter = painterResource(R.drawable.ic_minimize), contentDescription = "Minimize Screen", tint = ComponentColorPrimary)
                }
            }
        }
    }

}