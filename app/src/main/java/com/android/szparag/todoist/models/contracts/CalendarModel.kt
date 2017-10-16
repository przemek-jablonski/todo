package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.utils.ReactiveListEvent
import io.reactivex.Observable

interface CalendarModel : Model {

  fun setupCalendarInstance()

  fun subscribeForDaysList(): Observable<ReactiveListEvent>

  //  fun updateLocale(locale: Locale)
//  fun setSelectedDay(dayNumberInTheWeek: Int): Observable<Int>
//  fun setSelectedDaySync(dayNumberInTheWeek: Int)
//
//  fun getSelectedDay(): Observable<com.android.szparag.todoist.models.entities.RenderDay>
//  fun getCurrentWeek(): Observable<RenderWeekDays>
//
  fun requestRelativeWeekAsDays(weekForward: Boolean, fetchMultiplier: Int)

  fun fillDaysListInitial()
//  fun fetchRelativeWeekAsDays(): Observable<RenderDay>


//  fun resetRelativeWeekAsDays()

}