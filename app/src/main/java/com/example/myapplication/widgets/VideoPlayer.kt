package com.example.myapplication.widgets

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import java.util.UUID

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    resources: List<Any>,
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer
            .Builder(context)
            .build()
            .apply {
                val items = listOf(resources.random()).getMediaItems()
                setMediaItems(items)
                prepare()
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ALL
            }
    }

    Box(modifier = modifier) {
        DisposableEffect(
            key1 = AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    StyledPlayerView(it).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        resizeMode = RESIZE_MODE_FILL
                        useController = false
                    }
                }
            )
        ) {
            onDispose {
                Log.d("VideoPlayer", "Releasing player")
                exoPlayer.release()
            }
        }

    }
}

private fun List<Any>.getMediaItems(): List<MediaItem> {
    return when {
        this.isEmpty() -> return emptyList()
        this[0] is String -> {
            mapIndexed { _, uri ->
                MediaItem.fromUri(Uri.parse(uri as String))
            }
        }
        this[0] is Int -> {
            mapIndexed { _, rawResource ->
                MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(rawResource as Int))
            }
        }
        else -> throw IllegalArgumentException("Unsupported resource type list")
    }
}