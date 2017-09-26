package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.events.RenderWeekDayEvent
import io.reactivex.Observable
import java.util.Calendar

interface CalendarModel: Model {

  val calendar: Calendar

  fun setupCalendarInstance()

  fun getCurrentDay()

  fun getCurrentWeek(): Observable<RenderWeekDayEvent>

}