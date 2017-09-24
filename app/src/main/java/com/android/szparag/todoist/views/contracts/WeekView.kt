package com.android.szparag.todoist.views.contracts

import com.android.szparag.todoist.AnimationEvent
import io.reactivex.Completable
import io.reactivex.Observable


interface WeekView : View {

  fun setupWeekList(): Completable  //todo: custom data structure in

  fun subscribeUserDayPicked(): Observable<Pair<android.view.View, Int>>

  fun animateWeekdayToFullscreen(view: android.view.View, positionInList: Int): Observable<AnimationEvent>
  fun animateShiftItemOnScreenPosition(positionInList: Int): Completable

}