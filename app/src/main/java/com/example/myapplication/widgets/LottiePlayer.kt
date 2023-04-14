package com.example.myapplication.widgets

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myapplication.R

@Composable
fun LottiePlayer(
    modifier: Modifier = Modifier,
    res: Int = R.raw.lottiee,
) {

    Box(modifier = modifier) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(res), cacheKey = null)

        LottieAnimation(
            modifier = Modifier.fillMaxWidth(),
            composition = composition,
        )

        val context = LocalContext.current
        DisposableEffect(key1 = Unit,
            effect = {
                onDispose {
                    LottieCompositionFactory.clearCache(context)
                    System.gc()
                }
            }
        )
    }

}