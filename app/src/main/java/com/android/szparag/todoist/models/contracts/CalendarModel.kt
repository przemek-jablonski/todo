package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.utils.ReactiveList
import com.android.szparag.todoist.utils.ReactiveListEvent
import io.reactivex.Observable
import org.joda.time.LocalDate

interface CalendarModel : Model {

  fun fetchInitialCalendarData(weekFetchMultiplier: Int)
  fun loadDaysFromCalendar(weekForward: Boolean, weeksCount: Int)

  fun subscribeForDaysListEvents(): Observable<ReactiveListEvent<LocalDate>>
  fun subscribeForDaysListData(): Observable<ReactiveList<LocalDate>>
}