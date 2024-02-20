package com.easyhz.pico.util

import com.google.firebase.Timestamp
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Timestamp.toDateFormat(): String =
    LocalDateTime.ofInstant(this.toDate().toInstant(), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm", Locale.getDefault()))

fun Timestamp.toDay(): Long {
    val expireDateTime = this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val currentDateTime = LocalDateTime.now()

    return Duration.between(currentDateTime, expireDateTime).toDays()
}
