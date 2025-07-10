package com.quix

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object MediaStoreHelper {
	suspend fun getAllVideoFolders(context: Context): List<VideoFolder> = withContext(Dispatchers.IO) {
    val folders = mutableMapOf<String, MutableList<VideoData>>() // Map path to list of videos in it
    val collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME, // The folder name
        MediaStore.Video.Media.DATA // Full path, useful for grouping
    )
    val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC" // Or by BUCKET_DISPLAY_NAME

    context.contentResolver.query(collection, projection, null, null, sortOrder)?.use { cursor ->
        val idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val bucketNameCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA) // Full path including filename

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idCol)
            val title = cursor.getString(titleCol)
            val duration = cursor.getLong(durationCol)
            val size = cursor.getLong(sizeCol)
            val bucketName = cursor.getString(bucketNameCol)
            val fullPath = cursor.getString(dataCol)
            val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

            val folderPath = fullPath.substringBeforeLast("/") // Get the directory path
            val video = VideoData(id, title, duration, size, contentUri)

            folders.getOrPut(folderPath) { mutableListOf() }.add(video)
        }
    }
    // Convert map to your list of VideoFolder objects
    folders.map { (path, videoList) -> VideoFolder(videoList.firstOrNull()?.let { File(path).name } ?: "Unknown", path, videoList.size) }
        .sortedBy { it.name } // Sort folders by name
}

// Function to get videos within a specific folder path
suspend fun getVideosInFolder(context: Context, folderPath: String): List<VideoData> = withContext(Dispatchers.IO) {
    val videosInFolder = mutableListOf<VideoData>()
    val collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DATA // Need this to filter by actual path
    )
    // Filter by the full path beginning with the folder path
    val selection = "${MediaStore.Video.Media.DATA} LIKE ?"
    val selectionArgs = arrayOf("$folderPath/%") // % is SQL wildcard

    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

    context.contentResolver.query(collection, projection, selection, selectionArgs, sortOrder)?.use { cursor ->
        val idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idCol)
            val title = cursor.getString(titleCol)
            val duration = cursor.getLong(durationCol)
            val size = cursor.getLong(sizeCol)
            val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

            videosInFolder.add(VideoData(id, title, duration, size, contentUri))
        }
    }
    videosInFolder
}
}
