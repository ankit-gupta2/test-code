package com.example.myapplication.spaces

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import com.example.myapplication.widgets.CustomText
import com.example.myapplication.widgets.TextPopup
import com.example.myapplication.widgets.getCustomTextWidgets

@Composable
fun ArbitrarySpace(
    modifier: Modifier = Modifier,
) {
    var coordinates : IntOffset? by remember { mutableStateOf(null) }

    Content(modifier = modifier) { layoutCoordinates ->
        val (x: Int, y: Int) = when {
            layoutCoordinates.isAttached -> with(layoutCoordinates.positionInRoot()) {
                if (y < 0) -1 to -1
                else x.toInt() to y.toInt()
            }

            else -> -1 to -1
        }

        coordinates = if (x == -1) null else IntOffset(x, y)
    }

    TextPopup(coordinates = coordinates) {
        CustomText(text = "Hello world", modifier = Modifier.background(Color.Green))
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    onCoordinatesAvailable: (LayoutCoordinates) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        getCustomTextWidgets(count = 25).forEach { ctw ->
            item {
                ctw()
            }
        }

        getCustomTextWidgets(
            modifier = Modifier
                .background(color = Color.Red)
                .onGloballyPositioned {
                    onCoordinatesAvailable(it)
                },
            count = 1
        ).forEach { ctw ->
            item {
                ctw()
            }
        }

        getCustomTextWidgets(count = 25).forEach { ctw ->
            item {
                ctw()
            }
        }
    }
}