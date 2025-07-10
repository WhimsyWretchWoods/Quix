package com.quix

import android.net.Uri

data class VideoData(
    val id: Long,
    val title: String,
    val duration: Long,
    val size: Long,
    val contentUri: Uri
)
