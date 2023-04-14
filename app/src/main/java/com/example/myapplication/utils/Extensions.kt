package com.example.myapplication.utils

import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot

fun Float.toOffset(): Offset {
    return Offset(x = 0f, y = this)
}

fun logD(msg : String, tag : String = "Custom log", ) {
    Log.d(tag, msg)
}

fun Modifier.onFullyVisible(onFullyVisible: () -> Unit) = composed {
    val (windowWidth, windowHeight) = remember {
        Resources.getSystem().displayMetrics.let { displayMetrics ->
            Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
    }

    // TODO: onGloballyPositioned is expensive.
    //  See https://kotlinlang.slack.com/archives/CJLTWPH7S/p1642594240234800?thread_ts=1642589856.232200&cid=CJLTWPH7S
    this.onGloballyPositioned { layoutCoordinates ->
        val (width, height) = layoutCoordinates.size
        val (x1, y1) = layoutCoordinates.positionInRoot()
        if (VisibilityUtil.isFullyVisible(windowWidth, windowHeight, width, height, x1, y1)) {
            onFullyVisible()
        }
    }
}