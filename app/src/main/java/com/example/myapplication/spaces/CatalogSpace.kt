package com.example.myapplication.spaces

import android.util.Log
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.myapplication.utils.URLConstants
import com.example.myapplication.widgets.Image

enum class CatalogType {
    GRID_VERTICAL,
    VERTICAL,
    HORIZONTAL,
    GRID_HORIZONTAL,
}

@Composable
fun CatalogSpace(modifier: Modifier = Modifier) {
    var catalogType by remember { mutableStateOf(CatalogType.HORIZONTAL) }

    Column(modifier = modifier) {
        CatalogTypeFilter(
            modifier = Modifier,
            currentSelection = catalogType,
            onSelection = { catalogType = it })

        Spacer(modifier = Modifier.height(16.dp))

        when (catalogType) {
            CatalogType.GRID_VERTICAL ->
                VerticalGridCatalogs(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colsCount = 2,
                    aspectRatio = 1f,
                )
            CatalogType.VERTICAL ->
                VerticalGridCatalogs(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colsCount = 1,
                    aspectRatio = 2.8f,
                )
            CatalogType.HORIZONTAL ->
                HorizontalGridCatalogs(
                    modifier = Modifier.fillMaxWidth(),
                    rows = 1,
                    aspectRatio = .5625f,
                    maxCatalogWidth = 144.dp
                )
            CatalogType.GRID_HORIZONTAL ->
                HorizontalGridCatalogs(
                    modifier = Modifier.fillMaxWidth(),
                    rows = 6,
                    aspectRatio = .5625f,
                    maxCatalogWidth = 144.dp
                )
        }
    }

}

@Composable
fun VerticalGridCatalogs(
    modifier: Modifier,
    colsCount: Int,
    items: List<String> = URLConstants.getList(20),
    aspectRatio: Float,
) {
    val state = rememberLazyGridState()
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(colsCount),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        state = state,
    ) {
        items(span = {
            GridItemSpan(maxCurrentLineSpan)
        }, items = (0..10).map { "Hello world" }) {
            Text(
                text = "Hello World",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
            )
        }

        items(items) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio),
                imageUrl = it
            )
        }
    }

    val isDragged by state.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(key1 = isDragged, block = {
        Log.d("interaction", "VerticalGridCatalogs: IsDragged : $isDragged")
    })
}

@Composable
fun HorizontalGridCatalogs(
    modifier: Modifier,
    rows: Int,
    items: List<String> = URLConstants.getList(1),
    aspectRatio: Float,
    maxCatalogWidth: Dp,
) {
    val state = rememberLazyGridState()

    LazyColumn {
        item {
            val rowsVerticalSpacing = 16.dp * (rows - 1)
            val height = remember { rows * (maxCatalogWidth / aspectRatio) + rowsVerticalSpacing }
            LazyHorizontalGrid(
                modifier = modifier.height(height),
                rows = GridCells.Fixed(rows),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = state,
            ) {
                items(items) {
                    Image(
                        modifier = Modifier
                            .width(maxCatalogWidth)
                            .aspectRatio(aspectRatio),
                        imageUrl = it
                    )
                }
            }
        }
    }

    val isDragged by state.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(key1 = isDragged, block = {
        Log.d("interaction", "VerticalGridCatalogs: IsDragged : $isDragged")
    })
}

@Composable
fun CatalogTypeFilter(
    modifier: Modifier,
    currentSelection: CatalogType,
    onSelection: (CatalogType) -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (type in CatalogType.values()) {
            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = if (currentSelection == type) Color.White else MaterialTheme.colors.primary,
                    contentColor = if (currentSelection == type) MaterialTheme.colors.primary else Color.White
                ),
                onClick = { onSelection(type) }
            ) {
                Text(text = type.name, fontSize = 12.sp)
            }
        }
    }
}
