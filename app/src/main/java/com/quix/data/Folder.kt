package com.quix.data

import android.net.Uri

data class FolderData(
    val name: String,
    val thumbnailUri: Uri,
    val bucketId: String
)
