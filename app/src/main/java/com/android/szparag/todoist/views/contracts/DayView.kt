package com.android.szparag.todoist.views.contracts

interface DayView : View {
  fun renderDay(dayNameHeader: CharSequence, dayNumber: Int, monthName: CharSequence, yearNumber: Int)
  fun renderTasks(tasksList: List<CharSequence>, tasksCompletedCount: Int, tasksRemainingCount: Int)
}