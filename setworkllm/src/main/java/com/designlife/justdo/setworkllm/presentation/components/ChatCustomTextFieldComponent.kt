package com.designlife.justdo.setworkllm.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.designlife.justdo.setworkllm.R
import com.designlife.justdo.setworkllm.ui.theme.ChatBackgroundColorLight
import com.designlife.justdo.setworkllm.ui.theme.ComponentColorGreen
import com.designlife.justdo.setworkllm.ui.theme.ComponentColorPrimary
import com.designlife.justdo.setworkllm.ui.theme.Red40
import com.designlife.justdo.setworkllm.ui.theme.Yellow40
import com.designlife.justdo.setworkllm.ui.theme.chatTextPlaceholderStyle
import com.designlife.justdo.setworkllm.ui.theme.chatTextStyle
import kotlinx.coroutines.delay


@Composable
fun ChatTextField(
    isThinking : Boolean,
    chatText : String,
    onChatTextEvent : (text: String) -> Unit,
    onChatButtonEvent : () -> Unit
) {

    var thinkText by remember {
        mutableStateOf("Thinking ")
    }

    var thinkingColor by remember {
        mutableStateOf(ComponentColorPrimary)
    }
    val animatedThinkingColor by animateColorAsState(
        targetValue = if (isThinking) thinkingColor else ComponentColorGreen,
        animationSpec = tween(durationMillis = 100),
        label = "Thinking"
    )

    LaunchedEffect(isThinking) {
        while (true){
            thinkText = "Thinking ."
            delay(150)
            thinkingColor = Yellow40
            thinkText = "Thinking .."
            delay(150)
            thinkingColor = ComponentColorPrimary
            thinkText = "Thinking ..."
            delay(150)
            thinkingColor = Red40
            thinkText = "Thinking .."
        }
    }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth(.8F)
            .wrapContentHeight()
            .border(width = 1.dp, color = ComponentColorPrimary, shape = RoundedCornerShape(16.dp)),
        value = chatText,
        onValueChange = {
            onChatTextEvent(it)
        },
        singleLine = false,
        cursorBrush = SolidColor(ComponentColorPrimary)
    ){ innerTextField ->
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                .height(60.dp)
                .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(5.dp).background(color = animatedThinkingColor, shape = CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(.8F),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(6.dp))
                if (chatText.isEmpty()){
                    Text(modifier = Modifier.padding(2.dp).fillMaxWidth(), text = if(isThinking) thinkText else "Ask anything ...", style = chatTextPlaceholderStyle.value, textAlign = TextAlign.Start)
                }else{
                    Text(modifier = Modifier.padding(2.dp).fillMaxWidth(), text = chatText, style = chatTextStyle.value, textAlign = TextAlign.Start)
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    onChatButtonEvent()
                }) {
                    Box(
                        modifier = Modifier.size(36.dp)
                            .background(color = Color.White, shape = CircleShape)
                            .border(width = 1.dp, color = ComponentColorPrimary, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(modifier = Modifier.size(20.dp), painter = painterResource(R.drawable.ic_send), contentDescription = "Send", tint = ComponentColorPrimary)
                    }
                }
            }
        }
    }

}