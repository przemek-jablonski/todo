package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.events.RenderWeekDayEvent
import com.android.szparag.todoist.models.entities.RenderWeekDay
import io.reactivex.Observable
import java.util.Calendar

interface CalendarModel: Model {

  val calendar: Calendar

  fun setupCalendarInstance()

  fun getCurrentDay(): Observable<RenderWeekDay>

  fun getCurrentWeek(): Observable<RenderWeekDayEvent>

}