package com.example.myapplication.widgets

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

@Composable
fun TextPopup(
    coordinates: IntOffset?,
    content: @Composable () -> Unit,
) {
    if (coordinates != null) {
        val popupPositionProvider = object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                return coordinates?.copy(
                    x = (windowSize.width - popupContentSize.width) / 2,
                    y = coordinates.y
                ) ?: IntOffset(0, 0)
            }
        }

        val isVisible = if (coordinates == null) false else coordinates.y >= 0
        AnimatedVisibility(visible = isVisible) {
            Popup(popupPositionProvider = popupPositionProvider) {
                content()
            }
        }
        LaunchedEffect(key1 = isVisible, block = {
            Log.d("testing", "TextPopup: $isVisible")
        })
    }
}