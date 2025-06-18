package com.quix.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.quix.MediaFetcher
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.clickable
import android.graphics.Bitmap
import coil.request.ImageRequest

@Composable
fun ImageGridView(bucketId: String?,onImageClick: (String)-> Unit) {
  val context = LocalContext.current
  val images = remember(bucketId) {
    if (bucketId.isNullOrEmpty()) emptyList()
    else MediaFetcher.fetchImagesInFolder(context, bucketId)
  }

  LazyVerticalGrid (
    GridCells.Fixed(3)
  ) {
    items(images) {
      image ->
      Card(
        modifier = Modifier.padding(8.dp).clickable {
          onImageClick(image.uri.toString())
        }
      ) {
        AsyncImage(
          model = ImageRequest.Builder(context)
          .data(image.uri)
          .bitmapConfig(Bitmap.Config.RGB_565)
          .build(),
          contentDescription = null,
          modifier = Modifier
          .aspectRatio(1f),
          contentScale = ContentScale.Crop
        )
      }
    }

  }

}