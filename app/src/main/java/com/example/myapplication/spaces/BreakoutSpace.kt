package com.example.myapplication.spaces

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.widgets.GifPlayer
import com.example.myapplication.widgets.LottiePlayer
import com.example.myapplication.widgets.VideoPlayer

@Composable
fun BreakoutSpace() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {

        VideoPlayer(
            modifier = Modifier.fillMaxSize(),
            resources = listOf<Any>(
                "https://www.youtube.com/watch?v=v0WkDsrgWoM",
                "https://static.videezy.com/system/resources/previews/000/005/553/original/Times_Square_Sidewalk.mp4",
                "https://static.videezy.com/system/resources/previews/000/021/600/original/Origami-Pig-Time-Lapse-2078.mp4",
                "https://static.videezy.com/system/resources/previews/000/046/981/original/beyaz_bulutlar_1_b11_c_5.mp4",
            )
        )

//        GifPlayer(
//            modifier = Modifier.fillMaxSize(),
//            resource = "https://res.cloudinary.com/cloudinary-marketing/images/c_fill,w_750/f_auto,q_auto/v1649720751/Web_Assets/blog/Mario_1/Mario_1-gif?_i=AA"
//        )
//
//        GifPlayer(
//            modifier = Modifier.fillMaxSize(),
//            resource = "https://s3.ap-southeast-1.amazonaws.com/images.deccanchronicle.com/dc-Cover-g6cpca63spmouog48p68fk20a0-20170530145539.Medi.jpeg"
//        )

//        LottiePlayer(
//            modifier = Modifier.fillMaxSize(),
//            res = R.raw.s
//        )
    }
}

@Composable
fun BreakoutSpaceContainer(
    modifier: Modifier = Modifier,
    count: Int = 1
) {
    LazyColumn(modifier = modifier) {
        items(count = count) {
            BreakoutSpace()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}