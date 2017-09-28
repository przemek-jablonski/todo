package com.android.szparag.todoist.views.contracts

import com.android.szparag.todoist.models.entities.RenderDay
import io.reactivex.Completable
import io.reactivex.Observable

interface DayView : View {

  fun setupCalendarHeader(headerImageUrl: String): Completable
  fun setupCalendarQuote(quoteText: String): Completable
  fun setupCalendarExtras(renderDay: RenderDay): Completable
  fun setupCalendarCheckList(renderDay: RenderDay): Completable
  fun setupCalendarBackingView(renderDay: RenderDay): Completable

  fun subscribeUserAddButtonClicked(): Observable<Any>
  fun subscribeUserChecklistItemCheck(): Observable<Any>
  fun subscribeUserChecklistItemLongPressed(): Observable<Any>

  fun animateCalendarHeader(): Completable
  fun animateCalendarQuote(): Completable
  fun animateCalendarChecklist(): Completable
  fun animateCalendarAddButton(): Completable

}