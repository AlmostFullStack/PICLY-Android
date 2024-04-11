package com.easyhz.picly.provider

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.easyhz.picly.R
import com.easyhz.picly.util.saveImage
import java.io.File

class PiclyFileProvider: FileProvider(R.xml.file_paths) {
    companion object {
        /**
         * 공유받은 이미지 캐시 파일 생성하는 함수
         * @return 캐시 파일 Content Uri
         */
        suspend fun getIncomingImageUri(context: Context, uri: Uri): Uri {
            try {
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
            } catch (e: Exception) {
                throw e
            }
        }
    }
}