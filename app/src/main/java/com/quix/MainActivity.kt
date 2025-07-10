package com.quix

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "folderList") {
                composable("folderList") {
                    FolderListScreen(navController = navController)
                }
                composable("videoList/{folderPath}") { backStackEntry ->
                    val folderPath = backStackEntry.arguments?.getString("folderPath") ?: ""
                    VideoListScreen(navController = navController, folderPath = folderPath)
                }
                composable("videoPlayer/{videoUri}") { backStackEntry ->
                    val videoUriString = backStackEntry.arguments?.getString("videoUri")
                    val videoUri = videoUriString?.let { Uri.parse(it) }
                    VideoPlayerScreen(videoUri = videoUri)
                }
            }
        }
    }
}

@Composable
fun FolderListScreen(navController: NavHostController) {
    val context = LocalContext.current
    var folders by remember { mutableStateOf<List<VideoFolder>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        folders = MediaStoreHelper.getAllVideoFolders(context)
        isLoading = false
    }

    if (isLoading) {
        Text("Loading folders...")
    } else if (folders.isEmpty()) {
        Text("No video folders found.")
    } else {
        LazyColumn {
            items(folders) { folder ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            val encodedPath = URLEncoder.encode(folder.path, StandardCharsets.UTF_8.toString())
                            navController.navigate("videoList/$encodedPath")
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = folder.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "${folder.videoCount} videos", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun VideoListScreen(navController: NavHostController, folderPath: String) {
    val context = LocalContext.current
    var videos by remember { mutableStateOf<List<VideoData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(folderPath) {
        isLoading = true
        val decodedPath = URLDecoder.decode(folderPath, StandardCharsets.UTF_8.toString())
        videos = MediaStoreHelper.getVideosInFolder(context, decodedPath)
        isLoading = false
    }

    if (isLoading) {
        Text("Loading videos in folder...")
    } else if (videos.isEmpty()) {
        Text("No videos found in this folder.")
    } else {
        LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            items(videos, key = { it.id }) { video ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val encodedUri = URLEncoder.encode(video.contentUri.toString(), StandardCharsets.UTF_8.toString())
                            navController.navigate("videoPlayer/$encodedUri")
                        }
                        .padding(8.dp)
                ) {
                    Text(text = "Title: ${video.title}")
                    Text(text = "Duration: ${video.duration / 1000}s")
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(videoUri: Uri?) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            if (videoUri != null) {
                val mediaItem = MediaItem.fromUri(videoUri)
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                exoPlayer.pause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            exoPlayer.release()
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
