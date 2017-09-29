package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.models.entities.RenderWeekDays
import io.reactivex.Observable
import java.util.Calendar
import java.util.Locale

interface CalendarModel: Model {

  fun setupCalendarInstance()

  fun updateLocale(locale: Locale)
  fun setSelectedDay(dayNumberInTheWeek: Int): Observable<Int>
  fun setSelectedDaySync(dayNumberInTheWeek: Int)

  fun getSelectedDay(): Observable<com.android.szparag.todoist.models.entities.RenderDay>
  fun getCurrentWeek(): Observable<RenderWeekDays>

}