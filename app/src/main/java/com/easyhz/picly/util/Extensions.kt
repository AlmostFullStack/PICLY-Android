package com.easyhz.picly.util

import android.database.Cursor
import android.net.Uri
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.easyhz.picly.data.entity.album.ImageSize
import com.easyhz.picly.data.firebase.AuthError
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.google.firebase.Timestamp
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

fun Timestamp.toDateFormat(): String =
    LocalDateTime.ofInstant(this.toDate().toInstant(), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm", Locale.getDefault()))

fun Timestamp.toDay(): Double {
    val expireDateTime = this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val currentDateTime = LocalDateTime.now()
    return Duration.between(currentDateTime, expireDateTime).toMinutes().toDouble()
}

fun Double.toDay(): Int = (this / 60 / 24).roundToInt()

fun Double.toDetailDay(): String = if(this <= 0) {
    "만료"
} else {
    val days = this / (60 * 24)
    val hours = (this % (60 * 24)) / 60
    val minutes = (this % 60)
    "${days.toInt()}일 ${hours.toInt()}시간 ${minutes.toInt()}분 후 만료"
}

fun Exception.unknownErrorCode() : String {
    return AuthError.ERROR_UNKNOWN.name
}

fun Cursor.getLongColumnOrThrow(columnName: String): Long =
    getLong(getColumnIndexOrThrow(columnName))

fun Cursor.getStringColumnOrThrow(columnName: String): String? =
    getString(getColumnIndexOrThrow(columnName))

fun View.animateGrow(isShow: Boolean, duration: Long = 100, tx: Float = 0.5f, ty: Float = 0.5f) {
    val visibilityState = if (isShow) View.VISIBLE else View.GONE

    scaleX = if (isShow) 0f else 1f
    scaleY = if (isShow) 0f else 1f

    if (isShow) {
        visibility = View.VISIBLE
        translationX = -width.toFloat() * tx
        translationY = height.toFloat() * ty
    }

    animate()
        .scaleX(if (isShow) 1f else 0f)
        .scaleY(if (isShow) 1f else 0f)
        .translationX(if (isShow) 0f else -width.toFloat() * tx)
        .translationY(if (isShow) 0f else height.toFloat() * ty)
        .setDuration(duration)
        .setInterpolator(AccelerateInterpolator())
        .withEndAction {
            visibility = visibilityState
        }
        .start()
}

fun List<GalleryImageItem>.getImageSizes(): List<ImageSize> =
    map { ImageSize(it.height, it.width) }

fun List<GalleryImageItem>.getImageUri(): List<Uri> =
    map { it.uri }

/**
 * 공유 url
 */

const val BASE_URL = "https://www.picly.app"
const val ALBUM = "Album"
fun String.toPICLY() = "$BASE_URL/$ALBUM/$this"
