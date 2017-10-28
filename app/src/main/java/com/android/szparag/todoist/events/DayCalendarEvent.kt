package com.android.szparag.todoist.events

import com.android.szparag.todoist.models.contracts.UnixTimestamp

data class DayCalendarEvent(
    val unixTimestamp: UnixTimestamp,
    val dayName: CharSequence,
    val dayNumber: Int,
    val monthNumber: Int,
    val monthName: CharSequence,
    val yearNumber: Int
)