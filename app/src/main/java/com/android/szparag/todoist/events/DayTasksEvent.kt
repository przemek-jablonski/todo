package com.android.szparag.todoist.events

import com.android.szparag.todoist.models.contracts.UnixTimestamp
import com.android.szparag.todoist.utils.invalidIntValue

data class DayTasksEvent(
    val unixTimestamp: UnixTimestamp,
    val tasksList: List<CharSequence>,
    val tasksCompletedCount: Int,
    val tasksRemaningCount: Int
) {
  companion object {
    fun empty(unixTimestamp: UnixTimestamp) = DayTasksEvent(unixTimestamp, emptyList(), invalidIntValue(), invalidIntValue())
  }
}