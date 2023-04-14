package com.example.myapplication.test

import android.widget.Toast
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun LaunchedEffect_KeyChangeTest(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val showToast: (String) -> Unit = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }

    var key = StringBuilder("Old value")
    LaunchedEffect(key1 = key) {
        showToast(Thread.currentThread().name)
    }

    Button(
        onClick = {
            key = StringBuilder("New value")
            showToast("Value changed")
        }
    ) {
        Text(text = "Change Value")
    }
}