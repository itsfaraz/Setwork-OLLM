package com.designlife.justdo.setworkllm.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.designlife.justdo.setworkllm.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

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
val NoInternetStyleFontSize = mutableStateOf<TextUnit>(24.sp)

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


val noInternetTextStyleOne = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = NoInternetStyleFontSize.value,
    fontWeight = FontWeight.Normal,
    lineHeight = 26.sp
))

val noInternetTextStyleTwo = mutableStateOf(TextStyle(
    color = ComponentColorPrimary,
    fontFamily = fontFamily,
    fontSize = NoInternetStyleFontSize.value,
    fontWeight = FontWeight.Normal,
    lineHeight = 26.sp
))
