@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.myapplication.layouts

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.zIndex
import com.example.myapplication.domain.model.*
import com.example.myapplication.domain.model.State
import com.example.myapplication.utils.logD
import com.example.myapplication.utils.rememberPageScrollState
import com.example.myapplication.utils.toOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.floor

val LocalHeaderVisibilityProvider = compositionLocalOf { false }

@Composable
fun FullHeightBottomSheet(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    toolbar: @Composable () -> Unit,
    header: @Composable () -> Unit,
    body: @Composable () -> Unit,
) {
    val state = rememberPageScrollState(initialValue = State.EXPANDED)
    var headerHeight by rememberSaveable { mutableStateOf(0f) }
    var onScrollForUpTriggered by remember { mutableStateOf(false) }
    var onScrollForDownTriggered by remember { mutableStateOf(false) }
    val measurements =
        rememberMeasurements(minHeight = headerHeight,  )
    val graphics =
        remember(measurements) { derivedStateOf { with(measurements) { state.getGraphics() } } }
    val connection =
        remember(headerHeight) { SheetScrollConnection(state, headerHeight) }
    val isHeaderVisible by remember { derivedStateOf { graphics.value.headerTranslationY > headerHeight } }

    val onScroll: (Boolean) -> Unit = { isScrollingUp ->
        coroutineScope.launch {
            state.animateTo(
                targetValue = if (isScrollingUp) State.COLLAPSED else State.EXPANDED,
                anim = tween(durationMillis = 500)
            )
        }
    }

    logD("$headerHeight")

//    LaunchedEffect(key1 = Unit) {
//        snapshotFlow { graphics.value.headerTranslationY }
//            .collectLatest { headerTranslationY ->
//                logD("$headerTranslationY", "txY")
//                if (headerTranslationY < minHeaderHeight) {
//                    swipeableState.performDrag(headerTranslationY - minHeaderHeight)
//                }
//                if (headerTranslationY > 0) {
//                    swipeableState.performDrag(headerTranslationY.unaryMinus())
//                }
//                if (headerTranslationY == minHeaderHeight) scrollDownTriggered = false
//                if (headerTranslationY == 0f) scrollUpTriggered = false
//
//                if (headerTranslationY < -5f && swipeableState.currentValue == State.EXPANDED && !scrollUpTriggered) {
//                    scrollUpTriggered = true
//                    onScroll(true)
//                }
//
//                if (headerTranslationY >= (minHeaderHeight - (-5f)) && swipeableState.currentValue == State.COLLAPSED && !scrollDownTriggered) {
//                    scrollDownTriggered = true
//                    onScroll(false)
//                }
//            }
//    }


    LaunchedEffect(key1 = Unit) {
        snapshotFlow { graphics.value.headerTranslationY }
            .collectLatest { headerTranslationY ->
                // control scroll over/underflow
                if (headerTranslationY < headerHeight) state.performDrag(headerTranslationY - headerHeight)
                if (headerTranslationY > 0f) state.performDrag(headerTranslationY.unaryMinus())

                if (headerTranslationY == headerHeight) onScrollForDownTriggered = false
                if (headerTranslationY == 0f) onScrollForUpTriggered = false

                val isScrollingUp = headerTranslationY <= -5f && state.currentValue == State.EXPANDED
                val isScrollingDown = headerTranslationY >= (headerHeight - (-5f)) && state.currentValue == State.COLLAPSED

                if (isScrollingUp && !onScrollForUpTriggered) {
                    onScrollForUpTriggered = true
                    onScroll(true)
                } else if (isScrollingDown && !onScrollForDownTriggered) {
                    onScrollForDownTriggered = true
                    onScroll(false)
                }
            }
    }

    BoxWithConstraints(modifier = Modifier) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .graphicsLayer {
                translationY = graphics.value.headerTranslationY
                scaleX = graphics.value.scaleX
                scaleY = graphics.value.scaleY
                alpha = graphics.value.alpha
            }
            .onGloballyPositioned { coordinates ->
                headerHeight = if (coordinates.size.height == 0) 0f else coordinates.size.height
                    .toFloat()
                    .unaryMinus()
            }

        ) {
//            CompositionLocalProvider(LocalHeaderVisibilityProvider provides isHeaderVisible) {
                header()
//            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .swipeable(
                    enabled = true,
                    state = state,
                    orientation = Orientation.Vertical,
                    anchors = mapOf(
                        headerHeight to State.COLLAPSED,
                        0f to State.EXPANDED,
                    )
                )
                .graphicsLayer {
                    translationY = graphics.value.bodyTranslationY
                }
                .nestedScroll(connection)
        ) {
            val overScrollConfiguration =
                if (graphics.value.bodyTranslationY == 0f) LocalOverscrollConfiguration.current else null
            CompositionLocalProvider(LocalOverscrollConfiguration provides overScrollConfiguration) { body() }
        }

//        Box(modifier = Modifier
//            .alpha(alpha = graphics.value.toolbarAlpha)
//            .graphicsLayer {
//                logD(
//                    "${graphics.value.toolbarTranslationY} / $toolbarHeight",
//                    "toolbar translation"
//                )
//                translationY = graphics.value.toolbarTranslationY
//            }
//            .onGloballyPositioned {
//                toolbarHeight = it.size.height
//                    .toFloat()
//                    .unaryMinus()
//            }) {
////            toolbar()
//        }
    }
}

@ExperimentalMaterialApi
private class SheetScrollConnection(
    val state: SwipeableState<State>,
    val minOffset: Float,
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        // if the user is scrolling up, then consume all scroll unless the browse sheet is full expanded to page
        return if (delta < 0 && state.offset.value > minOffset) {
            state.performDrag(delta).toOffset()
        } else
            super.onPreScroll(available, source)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = available.y
        logD("$delta", "onPostSroll")
        return state.performDrag(delta).toOffset()
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val velocity = available.y
        // swiping up and browse sheet is not yet and expanded fully
        return if (velocity < 0 && state.offset.value > minOffset) {
            logD("onPreFling", "connection")
            state.performFling(floor(velocity))
            available
        } else {
            super.onPreFling(available)
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        logD("onPostFling", "connection")
        state.performFling(available.y)
        return super.onPostFling(consumed, available)
    }
}

private data class SheetMeasurements(
    val density: Density,
    val minHeight: Float,
) {
    fun scaleX(offset: Float) = 1 - (offset / minHeight * 0.2f)
    fun scaleY(offset: Float) = 1 - (offset / minHeight * 0.1f)
    fun alpha(offset: Float): Float {
        return 1 - ((offset) / (minHeight / 1.2f))
    }

    fun toolbarAlpha(offset: Float): Float {
        return offset / minHeight
    }

    fun headerTranslationY(offset: Float) = offset
    fun bodyTranslationY(offset: Float) = minHeight * -1 + offset

//    fun toolbarTranslationY(offset: Float): Float {
//        return if (toolbarHeight / 4 - offset > 0) 0f else toolbarHeight / 4 - offset
//    }

    @ExperimentalMaterialApi
    fun PageScrollState.getGraphics(): Graphics {
        val offset = offset.value

        return Graphics(
            headerTranslationY = headerTranslationY(offset),
            bodyTranslationY = bodyTranslationY(offset),
            scaleX = scaleX(offset),
            scaleY = scaleY(offset),
            alpha = alpha(offset),
            toolbarAlpha = toolbarAlpha(offset),
            minHeight = minHeight,
//            toolbarTranslationY = toolbarTranslationY(offset)
        )
    }
}

@Composable
private fun rememberMeasurements(minHeight: Float): SheetMeasurements {
    val density = LocalDensity.current
    return remember(density, minHeight, ) {
        SheetMeasurements(
            density = density,
            minHeight = if (minHeight == 0f) 1f else minHeight,
        )
    }
}