package com.easyhz.picly.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.easyhz.picly.R
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.firebase.Timestamp
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
    val baseColor = ContextCompat.getColor(context, R.color.skeletonDark)
    val highlightColor = ContextCompat.getColor(context, R.color.skeletonLight)

    val shimmer = Shimmer.AlphaHighlightBuilder()
        .setDuration(1800)
        .setBaseAlpha(0.7f)
        .setHighlightAlpha(1f)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setFixedHeight(700)
        .setAutoStart(true)
        .build()
    return ShimmerDrawable().apply { setShimmer(shimmer) }
}