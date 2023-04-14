package com.example.myapplication.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myapplication.spaces.TestSpace
import com.example.myapplication.test.LaunchedEffect_KeyChangeTest
import com.example.myapplication.widgets.getCustomTextWidgets

@Composable
fun TestPage1() {
    TestSpace(widgets = { RenderableWidgets(modifier = it) })
}

@Composable
private fun RenderableWidgets(modifier: Modifier) {
    Box(modifier = Modifier.background(color = Color.Cyan)) {
        LazyColumn {
            items(items = getCustomTextWidgets(count = 40)) {
                it()
            }
        }

    }

//    LaunchedEffect_KeyChangeTest(modifier = modifier)
}
