package com.example.myapplication.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun GifPlayer(
    modifier : Modifier = Modifier,
    resource : Any,
) {
    val context = LocalContext.current

    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(context).data(resource).build(),
            contentDescription = "panda gif",
        )
    }

}