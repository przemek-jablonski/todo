package com.android.szparag.todoist.views.contracts

import com.android.szparag.todoist.models.entities.RenderDay
import io.reactivex.Completable
import io.reactivex.Observable

interface DayView : View {

  fun setupCalendarHeader(headerImageUrl: String): Observable<Boolean>
  fun setupCalendarQuote(quoteText: String): Observable<Boolean>
  fun setupCalendarExtras(renderDay: RenderDay): Observable<Boolean>
  fun setupCalendarCheckList(renderDay: RenderDay): Observable<Boolean>
  fun setupCalendarBackingView(renderDay: RenderDay): Observable<Boolean>

  fun subscribeUserAddButtonClicked(): Observable<Any>
  fun subscribeUserChecklistItemCheck(): Observable<Any>
  fun subscribeUserChecklistItemLongPressed(): Observable<Any>

  fun animateCalendarHeader(): Completable
  fun animateCalendarQuote(): Completable
  fun animateCalendarChecklist(): Completable
  fun animateCalendarAddButton(): Completable

}