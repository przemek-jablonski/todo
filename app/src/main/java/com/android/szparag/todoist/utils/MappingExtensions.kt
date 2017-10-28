package com.android.szparag.todoist.utils

import com.android.szparag.todoist.events.DayCalendarEvent
import com.android.szparag.todoist.events.DayTasksEvent
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.models.entities.TodoistDay
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.Locale

//todo make DayCalendarEvent and TasksDayEvent
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


fun LocalDate.toDayCalendarEvent(locale: Locale) = DayCalendarEvent(
    unixTimestamp = this.dayUnixTimestamp(),
    dayName = this.dayOfWeek().getAsText(locale),
    dayNumber = this.dayOfMonth,
    monthNumber = this.monthOfYear,
    monthName = this.monthOfYear().getAsText(locale),
    yearNumber = this.year
)

fun DateTime.toDayCalendarEvent(locale: Locale) = toLocalDate().toDayCalendarEvent(locale)

fun TodoistDay.toDayTasksEvent() = DayTasksEvent(
    unixTimestamp = this.unixTimestamp,
    tasksList = tasksNameList(),
    tasksCompletedCount = this.getDoneTasksCount(),
    tasksRemaningCount = this.getRemainingTasksCount()
)

fun TodoistDay.tasksNameList() = this.tasks.map { it.name }