package com.android.szparag.todoist.views.contracts

import com.android.szparag.todoist.AnimationEvent
import io.reactivex.Observable


interface WeekView : View {

  fun onUserDayPicked(): Observable<Pair<android.view.View, Int>>
  fun setupWeekdaysList()
  fun resizeDayToFullscreen(view: android.view.View, positionInList: Int): Observable<AnimationEvent>
  fun fixPositionByScrolling(positionInList: Int)

}