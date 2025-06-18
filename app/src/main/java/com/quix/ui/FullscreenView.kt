package com.quix.ui

import android.net.Uri
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun FullscreenView(imageUriString: String) {
  val context = LocalContext.current
  val imageUri = remember(imageUriString) { Uri.parse(imageUriString) }

  var scale by remember { mutableStateOf(1f) }
  var offsetX by remember { mutableStateOf(0f) }
  var offsetY by remember { mutableStateOf(0f) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
          scale = (scale * zoom).coerceIn(1f, 5f)
          offsetX += pan.x
          offsetY += pan.y
        }
      },
    contentAlignment = Alignment.Center
  ) {
    AsyncImage(
      model = ImageRequest.Builder(context)
        .data(imageUri)
        .crossfade(true)
        .build(),
      contentDescription = null,
      contentScale = ContentScale.Fit,
      modifier = Modifier
        .fillMaxSize()
        .graphicsLayer(
          scaleX = scale,
          scaleY = scale,
          translationX = offsetX,
          translationY = offsetY
        )
    )
  }
}
