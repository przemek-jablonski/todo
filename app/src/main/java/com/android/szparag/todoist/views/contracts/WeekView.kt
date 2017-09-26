package com.android.szparag.todoist.views.contracts

import com.android.szparag.todoist.AnimationEvent
import com.android.szparag.todoist.events.RenderWeekDayEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


interface WeekView : View {

  fun setupWeekList(renderWeekDaysEvent: RenderWeekDayEvent): Completable
  fun disposeWeekList(): Completable

  fun subscribeUserDayPicked(): Observable<Pair<android.view.View, Int>>

  fun animateWeekdayToFullscreen(view: android.view.View, positionInList: Int): Observable<AnimationEvent>

  fun animateShiftItemOnScreenPosition(positionInList: Int): Completable

}