package com.easyhz.picly.provider

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.easyhz.picly.R
import com.easyhz.picly.util.saveImage
import java.io.File

class PiclyFileProvider:FileProvider(R.xml.file_paths) {
    companion object {
        suspend fun getIncomingImageUri(context: Context, uri: Uri): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "incoming_image_",
                ".jpeg",
                directory,
            )
            context.saveImage(uri, file)
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}