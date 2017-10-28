package com.android.szparag.todoist.views.contracts

import io.reactivex.Observable

interface DayView : View {
  fun renderDay(dayNameHeader: CharSequence, dayNumber: Int, monthName: CharSequence, yearNumber: Int)
  fun renderTasks(tasksList: List<CharSequence>, tasksCompletedCount: Int, tasksRemainingCount: Int)
  fun subscribeNewTaskTextAccepted(): Observable<CharSequence>
}