package com.easyhz.picly.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
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

/**
 * dp to Px
 *
 * @return Int
 */
fun Int.toPx(context: Context): Int {
    val density: Float = context.resources.displayMetrics.density
    return (this.toFloat() * density).roundToInt()
}



/**
 *  firebase TimeStamp 를 page_album 의 item_album regDate에 들어갈 DateFormat으로 바꾸는 확장 함수
 *
 *  @return string : pattern(ex: yyyy.MM.dd HH:mm) 형식의 날짜
 */
fun Timestamp.toDateFormat(pattern: String = "yyyy.MM.dd HH:mm"): String =
    LocalDateTime.ofInstant(this.toDate().toInstant(), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))

/**
 *  firebase TimeStamp 와 현재의 차이를 분으로 계산하는 확장 함수
 *
 *  @return Double
 */
fun Timestamp.toDay(): Double {
    val expireDateTime = this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val currentDateTime = LocalDateTime.now()
    return Duration.between(currentDateTime, expireDateTime).toMinutes().toDouble()
}

/**
 *  현재와의 차이값인 `min: double`을 날짜로 계산하는 확장 함수
 *
 *  @return Int
 */
fun Double.toDay(): Int = (this / 60 / 24).roundToInt()

/**
 * 현재와의 차이값인 `min: double`을 상세한 날짜로 계산하는 확장 함수
 *
 * @return String : d일 h시간 m분 후 만료
 */
fun Double.toDetailDay(): String = if(this <= 0) {
    "만료"
} else {
    val days = this / (60 * 24)
    val hours = (this % (60 * 24)) / 60
    val minutes = (this % 60)
    "${days.toInt()}일 ${hours.toInt()}시간 ${minutes.toInt()}분 후 만료"
}

/**
 * 알 수 없는 에러 처리
 */
fun Exception.unknownErrorCode() : String {
    return AuthError.ERROR_UNKNOWN.name
}

/**
 * ContentResolver cursor 에서 Long 을 가져오는 확장 함수
 *
 * @return Long
 */
fun Cursor.getLongColumnOrThrow(columnName: String): Long? = try {
    getLong(getColumnIndexOrThrow(columnName))
} catch (e: Exception) {
    Log.d("getLongColumnOrThrow", "error > $e")
    null
}

/**
 * ContentResolver cursor 에서 String 을 가져오는 확장 함수
 *
 * @return String
 */
fun Cursor.getStringColumnOrThrow(columnName: String): String? =
    getString(getColumnIndexOrThrow(columnName))

/**
 * grow & shrink 애니메이션을 주는 확장 함수
 */
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

/**
 * GalleryImageItem 으로 이루어진 리스트에서 ImageSize만 구하는 확장 함수
 *
 * @return List<ImageSize>
 */
fun List<GalleryImageItem>.getImageSizes(): List<ImageSize> =
    map { ImageSize(it.height, it.width) }

/**
 * GalleryImageItem 으로 이루어진 리스트에서 Uri만 구하는 확장 함수
 *
 * @return List<Uri>
 */
fun List<GalleryImageItem>.getImageUri(): List<Uri> =
    map { it.uri }

/**
 * 공유 url
 */

const val BASE_URL = "https://www.picly.app"
const val ALBUM = "Album"
fun String.toPICLY() = "$BASE_URL/$ALBUM/$this"
