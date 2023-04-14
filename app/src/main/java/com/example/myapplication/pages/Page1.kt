package com.example.myapplication.pages

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.myapplication.layouts.FullHeightBottomSheet
import com.example.myapplication.layouts.LocalHeaderVisibilityProvider
import com.example.myapplication.utils.logD
import com.example.myapplication.utils.onFullyVisible
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Page1(onclick: () -> Unit) {
    FullHeightBottomSheet(
        toolbar = {
            Text(
                text = "Toolbar", modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.Black)
            )
        },
        header = {
//            val context = LocalContext.current
//            Button(
//                onClick = {
//                    Toast.makeText(context, "Hello world", Toast.LENGTH_SHORT).show() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp),
//            ) {
//                Text(
//                    text = "Header",
//                    textAlign = TextAlign.Center,
//                )
//            }

            Box(
                modifier = Modifier
                    .background(color = Color.Green)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),

            )
            {
                Text(
                    text = "Header",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black
                                )
                            )
                        )
                        .width(80.dp),
                )
            }
        }
    ) {
//                        Column(
//                            Modifier
//                                .fillMaxWidth()
//                                .background(Color.Green)
//                        ) {
//                            Text(text = "Content 1", modifier = Modifier.height(300.dp))
//                            Text(text = "Content 2", modifier = Modifier.height(300.dp))
//                            Text(text = "Content 3", modifier = Modifier.height(300.dp))
//                            Text(text = "Content 4", modifier = Modifier.height(300.dp))
//                            Text(text = "Content 5", modifier = Modifier.height(300.dp))
        BoxWithConstraints() {
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
    }
}