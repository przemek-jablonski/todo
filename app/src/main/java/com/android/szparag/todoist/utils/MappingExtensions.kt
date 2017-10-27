package com.android.szparag.todoist.utils

import com.android.szparag.todoist.models.entities.RenderDay
import org.joda.time.LocalDate
import java.util.Locale

//todo make CalendarDayEvent and TasksDayEvent
fun LocalDate.toRenderDay(locale: Locale, tasksList: List<String> = emptyList(),
    tasksCompletedCount: Int = invalidIntValue(), tasksRemainingCount: Int = invalidIntValue()) =
    RenderDay(
        unixTimestamp = this.dayUnixTimestamp(),
        dayName = this.dayOfWeek().getAsText(locale),
        dayNumber = this.dayOfMonth,
        monthNumber = this.monthOfYear,
        monthName = this.monthOfYear().getAsText(locale),
        yearNumber = this.year,
        tasksList = tasksList,
        tasksCompletedCount = tasksCompletedCount,
        tasksRemainingCount = tasksRemainingCount
    )