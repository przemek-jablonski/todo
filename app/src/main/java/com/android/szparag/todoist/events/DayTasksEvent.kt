package com.android.szparag.todoist.events

import com.android.szparag.todoist.models.contracts.UnixTimestamp

data class DayTasksEvent(
    val unixTimestamp: UnixTimestamp,
    val tasksList: List<CharSequence>,
    val tasksCompletedCount: Int,
    val tasksRemaningCount: Int
)