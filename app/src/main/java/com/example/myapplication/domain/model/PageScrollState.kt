package com.example.myapplication.domain.model

import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState

@ExperimentalMaterialApi
class PageScrollState(
    initialValue: State,
) : SwipeableState<State>(initialValue) {

    companion object {
        /**
         * The default [Saver] implementation for [PageScrollState].
         */
        fun Saver() = androidx.compose.runtime.saveable.Saver<PageScrollState, State>(
            save = { it.currentValue },
            restore = { PageScrollState(it) }
        )
    }
}