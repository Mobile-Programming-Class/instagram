package com.finject.instagram.interfaces

import android.net.Uri

interface HandleGalleryIntent {
    fun handleGalleryIntent( filePath: Uri? = null )
}