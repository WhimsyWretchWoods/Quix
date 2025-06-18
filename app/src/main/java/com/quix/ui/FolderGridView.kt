package com.quix.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.quix.MediaFetcher

@Composable
fun FolderGridView(onFolderClick: (String) -> Unit) {

  val context = LocalContext.current
  val folders = remember {
    MediaFetcher.fetchFolders(context)
  }

  LazyVerticalGrid(
    GridCells.Fixed(2),
    modifier = Modifier.padding(8.dp)
  ) {
    items(folders) {
      folder ->
      Card(
        modifier = Modifier
        .padding(8.dp)
        .clickable {onFolderClick(folder.bucketId) }
      ) {
        AsyncImage(
          model = folder.thumbnailUri,
          contentDescription = null,
          modifier = Modifier
          .aspectRatio(1f),
          contentScale = ContentScale.Crop
        )
        Text(
          text = folder.name,
          modifier = Modifier.padding(8.dp)
        )
      }
    }
  }
}
