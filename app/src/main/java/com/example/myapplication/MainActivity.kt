package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.myapplication.domain.model.Page
import com.example.myapplication.pages.Page1
import com.example.myapplication.pages.Page2
import com.example.myapplication.pages.Page3
import com.example.myapplication.pages.TestPage1
import com.example.myapplication.spaces.ArbitrarySpace
import com.example.myapplication.spaces.BreakoutSpaceContainer
import com.example.myapplication.spaces.CatalogSpace
import com.example.myapplication.spaces.SnappySpace
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                initResources()

                var pageType by remember { mutableStateOf(Page.PAGE_1) }
                val togglePage =
                    { pageType = if (pageType == Page.PAGE_1) Page.PAGE_2 else Page.PAGE_1 }

                when (pageType) {
                    Page.PAGE_1 -> Page1(onclick = togglePage)
                    Page.PAGE_2 -> Page2(onBackPress = togglePage, content = { SnappySpace() })
                    Page.PAGE_3 -> Page3(onclick = togglePage)
                    Page.TEST_PAGE_1 -> TestPage1()
                }
            }
        }
    }

    private fun initResources() {
        initCoil()
    }

    private fun initCoil() {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                }else {
                    add(GifDecoder.Factory())
                }
            }.build()

        Coil.setImageLoader(imageLoader = imageLoader)
    }
}

