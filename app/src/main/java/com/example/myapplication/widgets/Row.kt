package com.example.myapplication.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.URLConstants

@Composable
fun CustomRow() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                modifier = Modifier.size(40.dp),
                model = URLConstants.SQUARE_IMAGE,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                CustomText(
                    text = "Hello world",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    maxLines = 1
                )

                CustomText(
                    text = "Welcome to the Earth",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    maxLines = 1
                )
            }
        }

        CustomText(
            text = "Earth has been a great habitat for all kind of organisms, you can find here Humans, Ants, Birds etc.",
            color = Color.LightGray,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(start = 56.dp)
                .fillMaxWidth(),
            maxLines = 3
        )

        AsyncImage(
            model = URLConstants.WIDE_IMAGE,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 56.dp)
                .heightIn(max = 156.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
        )

        Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth().padding(start = 56.dp, top = 8.dp)) {
            CustomText(
                text = "Destroy",
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
private fun CustomRowPreview() {
    MyApplicationTheme() {
        Surface() {
            CustomRow()
        }
    }
}