package com.designlife.justdo.setworkllm.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.designlife.justdo.setworkllm.ui.theme.fontFamily

@Composable
fun ChatNotAvailableComponent(
    message : String
) {
    Text(modifier = Modifier.padding(top = 10.dp).fillMaxWidth(), text = message, textAlign = TextAlign.Center, fontFamily = fontFamily, fontSize = 14.sp)
}