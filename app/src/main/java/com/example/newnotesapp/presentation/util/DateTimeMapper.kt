package com.example.newnotesapp.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDateTime(): LocalDateTime {
    val instant = Instant.ofEpochMilli(this)
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toUtcTimestamp(): Long {
    val zonedDateTime = this.atZone(ZoneId.systemDefault())
    return zonedDateTime.toInstant().toEpochMilli()
}