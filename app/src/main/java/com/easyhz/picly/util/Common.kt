package com.easyhz.picly.util

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat
import com.easyhz.picly.R
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.firebase.Timestamp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.abs

const val PATTERN = "yyyy. M. d. h:mm a"
const val DATE_PATTERN = "yyyy. M. d."
const val TIME_PATTERN = "HH:m"
const val TIME_PATTERN_24 = "h:mm a"
const val PICLY = "PICLY"

fun getScreenWidth(context: Context): Int = context.resources.displayMetrics.widthPixels

fun getToday(): LocalDate = LocalDate.now()

fun getTime(): LocalTime = LocalTime.now()

fun getNextWeek(): LocalDate = getToday().plusWeeks(1)
fun getNextYear(): LocalDate = getToday().plusYears(1)

fun convertToDateFormat(year: Int, month: Int, dayOfMonth: Int): String = "$year. $month. $dayOfMonth."

fun convertToTimeFormat(hour: Int, minute: Int): String = LocalTime.of(hour, minute).withHour(hour).withMinute(minute).toTimeFormat24()

fun calculatePeriod(date: String, time: String): String {
    val givenDateTime = LocalDateTime.parse("$date $time", DateTimeFormatter.ofPattern(PATTERN))
    val currentDateTime = LocalDateTime.now()
    val duration = Duration.between(currentDateTime, givenDateTime)
    val days = abs(duration.toDays())
    val hours = abs(duration.toHours() % 24)

    return "$days 일 $hours 시간 후"
}

fun String.toFirebaseTimestamp(): Timestamp {
    val dateFormat = DateTimeFormatter.ofPattern(PATTERN)
    val instant = LocalDateTime.parse(this, dateFormat)
        .atZone(ZoneId.systemDefault())
        .toInstant()
    return Timestamp(instant.epochSecond, instant.nano)
}



fun LocalDate.toMs(): Long = this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

fun LocalDate.toDateFormat(): String {
    val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
    return this.format(formatter)
}

fun LocalTime.toTimeFormat(): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN)
    return this.format(formatter)
}

fun LocalTime.toTimeFormat24(): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN_24)
    return this.format(formatter)
}

fun getShimmerDrawable(context: Context): ShimmerDrawable {
    val baseColor = ContextCompat.getColor(context, R.color.collectionViewCellBackground)
    val highlightColor = ContextCompat.getColor(context, R.color.skeletonDark)

    val shimmer = Shimmer.ColorHighlightBuilder()
        .setDuration(1800)
        .setHighlightColor(highlightColor)
        .setBaseColor(baseColor)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setFixedHeight(700)
        .setAutoStart(true)
        .build()
    return ShimmerDrawable().apply { setShimmer(shimmer) }
}

fun getDefaultImage(context: Context): File? = try {
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_image)

    val filePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File(filePath, "default_image.png")
    FileOutputStream(file).use { fos ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
    }
    file
} catch (e: IOException) {
    e.printStackTrace()
    null
}

fun haptic(context: Context, durationMillis: Long) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    vibrator.vibrate(VibrationEffect.createOneShot(durationMillis, 30))
}
