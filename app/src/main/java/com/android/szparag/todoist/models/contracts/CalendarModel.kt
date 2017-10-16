package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.utils.ReactiveList
import com.android.szparag.todoist.utils.ReactiveListEvent
import io.reactivex.Observable
import org.joda.time.LocalDate

interface CalendarModel : Model {

  fun setupCalendarInstance()

  fun subscribeForDaysListEvents(): Observable<ReactiveListEvent>
  fun subscribeForDaysListData(): Observable<ReactiveList<LocalDate>>

  //  fun updateLocale(locale: Locale)
//  fun setSelectedDay(dayNumberInTheWeek: Int): Observable<Int>
//  fun setSelectedDaySync(dayNumberInTheWeek: Int)
//
//  fun getSelectedDay(): Observable<com.android.szparag.todoist.models.entities.RenderDay>
//  fun getCurrentWeek(): Observable<RenderWeekDays>
//
  fun requestRelativeWeekAsDays(weekForward: Boolean, fetchMultiplier: Int)

  fun fillDaysListInitial()
  fun mapToRenderDay(date: LocalDate): RenderDay
//  fun fetchRelativeWeekAsDays(): Observable<RenderDay>


//  fun resetRelativeWeekAsDays()

}