package com.android.szparag.todoist.views.contracts

interface DayView : View {
  fun renderDay(dayNameHeader: CharSequence, dayCalendarSubtitle: CharSequence, tasksCompletedCount: Int, tasksRemainingCount: Int)
  fun renderTasks(tasksList: List<String>)
}