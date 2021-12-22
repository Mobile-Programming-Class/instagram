package com.finject.instagram.interfaces

import android.graphics.Bitmap

interface HandleCameraIntent {
    fun handleCameraIntent( photo : Bitmap? = null );
}