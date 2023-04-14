package com.example.myapplication.spaces

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myapplication.widgets.getCustomTextWidgets

@Composable
fun TestSpace(modifier: Modifier = Modifier, widgets : @Composable (Modifier) -> Unit) {
    widgets(modifier)
}
