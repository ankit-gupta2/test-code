package com.example.myapplication.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.layouts.FullHeightPageLayout

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun Page3(onclick: () -> Unit) {
    FullHeightPageLayout(
        header = {
            Text(
                text = "Header",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(color = Color.Green),
                textAlign = TextAlign.Center
            )
        },
        body = {
            BoxWithConstraints {
                LazyColumn(
                    modifier = Modifier
                        .background(Color.Green)
                        .fillMaxSize(),
                ) {
                    for (i in 0..100) {
                        item {
                            Button(
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth(),
                                onClick = onclick
                            ) { Text(text = "Item $i") }
                        }
                    }
                }
            }
        })
}
