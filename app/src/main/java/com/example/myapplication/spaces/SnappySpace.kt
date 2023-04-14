@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package com.example.myapplication.spaces

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.example.myapplication.R
import com.example.myapplication.widgets.CustomText
import com.example.myapplication.widgets.LottiePlayer
import com.example.myapplication.widgets.VideoPlayer

@Composable
private fun Header() {
    Box(modifier = Modifier.height(250.dp))
//    VideoPlayer(
//        modifier = Modifier
//            .height(250.dp)
//            .fillMaxWidth(),
//        resources = listOf<Any>(R.raw.mobile)
//    )
}


@Composable
private fun LazyList() {
    LazyColumn {
        items(20) {
            CustomText(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(color = Color.DarkGray),
                text = "Hello $it",
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .background(color = Color.Black)
            )
        }
    }
}

enum class States {
    EXPANDED,
    COLLAPSED
}

@Composable
fun SnappySpace() {
    val swipeableState = rememberSwipeableState(initialValue = States.EXPANDED)
    val headerHeight = with(LocalDensity.current) { 250.dp.toPx() }

    BoxWithConstraints {
        var minHeight by remember { mutableStateOf(0f) }
        val headerVisibilityRatio = 1 - swipeableState.offset.value / minHeight

        val isNonScrollableTrayVisible by remember { derivedStateOf { swipeableState.offset.value > minHeight } }
        var isNonScrollableTrayHeightCalculated by remember { mutableStateOf(false) }
        Log.d("snappy", "SnappySpace: ${swipeableState.offset.value} : $minHeight : $isNonScrollableTrayVisible")

        val connection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        swipeableState.performDrag(delta).toOffset()
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return swipeableState.performDrag(delta).toOffset()
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    return if (available.y < 0 && swipeableState.offset.value > minHeight) {
                        swipeableState.performFling(available.y)
                        available
                    } else {
                        Velocity.Zero
                    }
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    swipeableState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }
        val scaleXx = 1 - (swipeableState.offset.value / minHeight * 0.1).toFloat()

        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            if (isNonScrollableTrayVisible || !isNonScrollableTrayHeightCalculated)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            translationY = swipeableState.offset.value
                            scaleX = 1 - (swipeableState.offset.value / minHeight * 0.1).toFloat()
                        }
                        .onSizeChanged { size ->
                            minHeight = size.height
                                .toFloat()
                                .unaryMinus()
                            isNonScrollableTrayHeightCalculated = true
                        },
                ) {
                    Header()
                }


            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .swipeable(
                        enabled = minHeight != 0f,
                        state = swipeableState,
                        orientation = Orientation.Vertical,
                        anchors = mapOf(
                            minHeight to States.COLLAPSED,
                            0f to States.EXPANDED,
                        )
                    )
                    .nestedScroll(connection)
                    .graphicsLayer {
                        translationY = headerHeight + swipeableState.offset.value
                    }
                //.verticalScroll(scrollState)
            ) {
                LazyList()
            }

            var coordinates by remember { mutableStateOf(IntOffset(0, 0)) }
            LaunchedEffect(key1 = swipeableState.offset.value, block = {
                coordinates = IntOffset(0, (swipeableState.offset.value + minHeight).toInt())
            })

            LottiePlayerPopup(
                scaleXx,
                headerVisibilityRatio,
                swipeableState.offset.value,
                coordinates
            ) {
                LottiePlayer()
            }
        }
    }
}


@Composable
private fun LottiePlayerPopup(
    scaleXx: Float,
    alpha: Float,
    offset: Float,
    coordinates: IntOffset,
    content: @Composable () -> Unit,
) {
    Box() {
        val popupPositionProvider = object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ) = IntOffset((windowSize.width - popupContentSize.width) / 2, coordinates.y)
        }

        Popup(popupPositionProvider = popupPositionProvider) {
            Box(modifier = Modifier
                .alpha(alpha)
                .graphicsLayer {
                    translationY = offset
                    scaleX = scaleXx
                }) {
                content()

            }
        }
    }
}
