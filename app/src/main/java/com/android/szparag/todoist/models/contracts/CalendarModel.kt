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

  fun requestRelativeWeekAsDays(weekForward: Boolean, fetchMultiplier: Int)

  //todo remove those two methods
  fun fillDaysListInitial()
  fun mapToRenderDay(date: LocalDate): RenderDay

}