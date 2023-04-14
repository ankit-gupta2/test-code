package com.example.myapplication.pages

import androidx.activity.compose.BackHandler
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.myapplication.widgets.CustomRow

@Composable
fun Page2(onBackPress: () -> Unit = {}, content : @Composable () -> Unit = { CustomRow() }) {
    Surface {
        BackHandler {
            onBackPress()
        }
    }
    content()
}
