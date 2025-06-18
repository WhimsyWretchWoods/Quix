package com.quix

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.quix.data.FolderData
import com.quix.data.ImageData

object MediaFetcher {

  fun fetchFolders(context: Context): List<FolderData> {
    val folderMap = mutableMapOf<String, FolderData>()
    val projection = arrayOf(
      MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
      MediaStore.Images.Media.BUCKET_ID,
      MediaStore.Images.Media._ID
    )
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

    context.contentResolver.query(uri, projection, null, null, sortOrder)?.use {
      cursor ->
      val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
      val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
      val bucketIdCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)

      while (cursor.moveToNext()) {
        val folderName = cursor.getString(nameCol)
        val bucketId = cursor.getString(bucketIdCol)
        val imageId = cursor.getLong(idCol)
        val imageUri = ContentUris.withAppendedId(uri, imageId)

        if (!folderMap.containsKey(bucketId)) {
          folderMap[bucketId] = FolderData(folderName, imageUri, bucketId)
        }
      }
    }
    return folderMap.values.toList()
  }

  fun fetchImagesInFolder(context: Context, bucketId: String): List<ImageData> {
    val images = mutableListOf<ImageData>()
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
    val selectionArgs = arrayOf(bucketId)
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

    context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)?.use {
      cursor ->
      val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

      while (cursor.moveToNext()) {
        val imageId = cursor.getLong(idCol)
        val imageUri = ContentUris.withAppendedId(uri, imageId)
        images.add(ImageData(imageUri))
      }
    }
    return images
  }
}