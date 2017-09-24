package com.android.szparag.todoist.views.contracts

import io.reactivex.Completable
import io.reactivex.Observable

interface DayView : View {

  fun setupCalendarHeader(headerImageUrl: String): Completable
  fun setupCalendarQuote(quoteText: String): Completable
  fun setupCalendarCheckList(): Completable //todo: custom data structure in

  fun subscribeUserAddButtonClicked(): Observable<Any>
  fun subscribeUserChecklistItemCheck(): Observable<Any>
  fun subscribeUserChecklistItemLongPressed(): Observable<Any>

  fun animateCalendarHeader(): Completable
  fun animateCalendarQuote(): Completable
  fun animateCalendarChecklist(): Completable
  fun animateCalendarAddButton(): Completable

}