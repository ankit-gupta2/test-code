package com.example.myapplication.utils

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.myapplication.domain.model.State
import com.example.myapplication.domain.model.PageScrollState


@Composable
@ExperimentalMaterialApi
fun rememberPageScrollState(initialValue: State): PageScrollState {
    return rememberSaveable(saver = PageScrollState.Saver()) {
        PageScrollState(initialValue)
    }
}
