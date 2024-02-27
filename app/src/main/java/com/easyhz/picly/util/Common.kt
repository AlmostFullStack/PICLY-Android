package com.easyhz.picly.util

import android.content.Context
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.abs

const val PATTERN = "yyyy. M. d. HH:mm a"
const val DATE_PATTERN = "yyyy. M. d."
const val TIME_PATTERN = "HH:m"
const val TIME_PATTERN_24 = "HH:mm a"

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