package com.android.szparag.todoist.views.contracts

import com.android.szparag.todoist.AnimationEvent
import com.android.szparag.todoist.events.ListScrollEvent
import com.android.szparag.todoist.models.entities.RenderDay
import io.reactivex.Observable

interface FrontView : View {

  fun animateShowBackgroundImage(): Observable<AnimationEvent>
  fun animateShowQuote(): Observable<AnimationEvent>
  fun animatePeekCalendar(): Observable<AnimationEvent>
  //todo: this return type is leaking separation concern (you wouldn't have RecyclerViewScrollEvent in iOS)
  fun subscribeDayListScrolls(): Observable<ListScrollEvent>

  fun appendRenderDays(appendingDays: List<RenderDay>, fromIndex: Int, changedElementsCount: Int)
  fun subscribeBackgroundClicked(): Observable<Any>
  fun randomizeContents()

}