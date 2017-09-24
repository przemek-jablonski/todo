package com.android.szparag.todoist.views.contracts

import io.reactivex.Completable
import io.reactivex.Observable

interface MonthView: View {

  fun setupMonthList(): Completable //todo: custom data structure in

  fun subscribeUserDayClicked(): Observable<Int>
  fun subscribeUserDayLongClicked(): Observable<Int>
  fun subscribeUserWeekClicked(): Observable<Int>
  fun subscribeUserWeekLongClicked(): Observable<Int>

  fun animateMonthListFadeIn(): Completable

}