package com.android.szparag.todoist.events

import com.android.szparag.todoist.models.contracts.UnixTimestamp
import com.android.szparag.todoist.utils.invalidIntValue

data class DayTasksEvent(
    val unixTimestamp: UnixTimestamp,
    val tasksList: List<CharSequence> = emptyList(),
    val tasksCompletedCount: Int = invalidIntValue(),
    val tasksRemaningCount: Int = invalidIntValue()
)