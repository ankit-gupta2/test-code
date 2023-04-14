package com.example.myapplication.utils

object VisibilityUtil {
    /**
     * "Fully visible" is subjective. For example,
     *  For rows, "fully visible" means when the height is fully visible.
     *  For columns, "fully visible" means when the width is fully visible.
     *  and for other composables, we'll need to consider both width and height.
     *
     *  To accommodate these 3 policies, we'll use the below enum.
     */
    private enum class FullyVisiblePolicy {
        WIDTH_HEIGHT,
        HEIGHT,
        WIDTH
    }

    internal fun isFullyVisible(
        windowWidth: Int,
        windowHeight: Int,
        width: Int,
        height: Int,
        x1: Float,
        y1: Float,
    ): Boolean {
        if (width == 0 || height == 0) return false

        val x2 = x1 + width
        val y2 = y1 + height

        val visiblePolicy = if (width > windowWidth && height <= windowHeight) {
            FullyVisiblePolicy.HEIGHT // Consider height only
        } else if (width <= windowWidth && height > windowHeight) {
            FullyVisiblePolicy.WIDTH // Consider width only
        } else {
            FullyVisiblePolicy.WIDTH_HEIGHT // Consider both width and height
        }

        return when (visiblePolicy) {
            FullyVisiblePolicy.WIDTH_HEIGHT -> {
                (x1 >= 0 && y1 >= 0) && (x2 <= windowWidth && y2 <= windowHeight)
            }
            FullyVisiblePolicy.WIDTH -> {
                x1 >= 0 && (x2 <= windowWidth)
            }
            FullyVisiblePolicy.HEIGHT -> {
                y1 >= 0 && (y2 <= windowHeight)
            }
        }
    }
}