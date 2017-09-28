package com.android.szparag.todoist.views.contracts

import com.android.szparag.todoist.AnimationEvent
import com.android.szparag.todoist.models.entities.RenderWeekDays
import io.reactivex.Completable
import io.reactivex.Observable


interface WeekView : View {

  fun setupWeekList(renderWeekDays: RenderWeekDays): Completable
  fun disposeWeekList(): Completable
  fun subscribeUserDayPicked(): Observable<Pair<android.view.View, Int>>
  fun animateWeekdayToFullscreen(view: android.view.View, positionInList: Int): Observable<AnimationEvent>
  fun animateShiftItemOnScreenPosition(positionInList: Int): Completable

}