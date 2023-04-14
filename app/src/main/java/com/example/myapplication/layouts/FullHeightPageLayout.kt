package com.example.myapplication.layouts

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
import com.example.myapplication.domain.model.*
import com.example.myapplication.domain.model.State
import com.example.myapplication.utils.logD
import com.example.myapplication.utils.rememberPageScrollState
import com.example.myapplication.utils.toOffset

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
fun FullHeightPageLayout(
    header: @Composable () -> Unit,
    body: @Composable () -> Unit,
) {
    var minHeaderHeightCalculated by rememberSaveable { mutableStateOf(false) }
    val swipeableState = rememberPageScrollState(initialValue = State.EXPANDED)
    var minHeaderHeight by rememberSaveable { mutableStateOf(0f) }
    val connection =
        remember(minHeaderHeight) { PageScrollConnection(swipeableState, minHeaderHeight) }
    val measurements = rememberMeasurements(minHeight = minHeaderHeight)

    val graphics =
        remember(measurements) {
            derivedStateOf {
                with(measurements) {
                    swipeableState.getGraphics()
                }
            }
        }

    BoxWithConstraints {
        Box(modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = graphics.value.headerTranslationY
                scaleX = graphics.value.scaleX
                scaleY = graphics.value.scaleY
                alpha = graphics.value.alpha
            }
            .onGloballyPositioned { coordinates ->
                minHeaderHeight = coordinates.size.height
                    .toFloat()
                    .unaryMinus()

                minHeaderHeightCalculated = true
            }) { header() }

        Box(modifier = Modifier
            .fillMaxSize()
            .let {
                if (minHeaderHeightCalculated) {
                    it.swipeable(
                        enabled = true,
                        state = swipeableState,
                        orientation = Orientation.Vertical,
                        anchors = mapOf(
                            minHeaderHeight to State.COLLAPSED,
                            0f to State.EXPANDED,
                        )
                    )
                } else {
                    it
                }
            }
            .nestedScroll(connection)
            .graphicsLayer {
                translationY = graphics.value.bodyTranslationY
            }) {
            val overScrollConfiguration =
                if (graphics.value.bodyTranslationY == 0f) LocalOverscrollConfiguration.current else null
            CompositionLocalProvider(LocalOverscrollConfiguration provides overScrollConfiguration) {
                body()
            }
        }
    }
}

@ExperimentalMaterialApi
private class PageScrollConnection(
    val state: SwipeableState<State>,
    val minOffset: Float,
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        logD(tag = "Page 3 scrolling", msg = delta.toString())
        // if the user is scrolling up, then consume all scroll unless the browse sheet is full expanded to page
        if (delta < 0 && state.offset.value > minOffset) {
            return state.performDrag(delta).toOffset()
        }

        return super.onPreScroll(available, source)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = available.y
        return state.performDrag(delta).toOffset()
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val velocity = available.y
        // swiping up and browse sheet is not yet and expanded fully
        return if (velocity < 0 && state.offset.value > minOffset) {
            state.performFling(velocity)
            available
        } else {
            super.onPreFling(available)
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        state.performFling(available.y)
        return super.onPostFling(consumed, available)
    }
}


private data class Measurements(
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
//            toolbarTranslationY = 0f
        )
    }
}

@Composable
private fun rememberMeasurements(minHeight: Float): Measurements {
    val density = LocalDensity.current
    return remember(density, minHeight) {
        Measurements(
            density = density,
            minHeight = minHeight
        )
    }
}


