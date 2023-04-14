package com.example.myapplication.widgets

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    textAlign: TextAlign? = null,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    text: String,
) {
    if (text == "This is #2")
        LaunchedEffect(
            key1 = Unit,
            block = {
                Log.v("testing", "launchedeffect called")
            }
        )

    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        textAlign = textAlign,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
    )
}

fun getCustomTextWidgets(modifier: Modifier = Modifier, count: Int): List<@Composable () -> Unit> {
    return (1..count).map {
        {
            CustomText(
                text = "This is #$it",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}