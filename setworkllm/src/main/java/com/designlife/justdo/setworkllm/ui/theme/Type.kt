package com.designlife.justdo.setworkllm.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.designlife.justdo.setworkllm.R

val fontFamily = FontFamily(
    Font(R.font.inter_regular),
    Font(R.font.inter_medium, weight = FontWeight.Medium),
    Font(R.font.inter_black, weight = FontWeight.Normal),
    Font(R.font.inter_bold, weight = FontWeight.Bold),
    Font(R.font.inter_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.inter_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.inter_light, weight = FontWeight.Light),
    Font(R.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(R.font.inter_thin, weight = FontWeight.Thin)
)

val headerStyleFontSize = mutableStateOf<TextUnit>(23.sp)
val headerDescriptionStyleFontSize = mutableStateOf<TextUnit>(18.sp)
val ChatStyleFontSize = mutableStateOf<TextUnit>(16.sp)

val chatViewHeaderStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = headerStyleFontSize.value,
    fontWeight = FontWeight.Normal
))

val chatViewDescriptionStyle = mutableStateOf(TextStyle(
    color = ChatTextColorLight,
    fontFamily = fontFamily,
    fontSize = headerDescriptionStyleFontSize.value,
    fontWeight = FontWeight.Light
))

val chatTextPlaceholderStyle = mutableStateOf(TextStyle(
    color = Color.Gray,
    fontFamily = fontFamily,
    fontSize = ChatStyleFontSize.value,
    fontWeight = FontWeight.Normal
))

val chatTextStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = ChatStyleFontSize.value,
    fontWeight = FontWeight.Normal
))

val chatReplyTextStyle = mutableStateOf(TextStyle(
    color = ChatReplyTextColorLight,
    fontFamily = fontFamily,
    fontSize = ChatStyleFontSize.value,
    fontWeight = FontWeight.Normal,
    lineHeight = 26.sp
))