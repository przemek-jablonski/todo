package com.android.szparag.todoist.views.contracts

import com.android.szparag.todoist.events.AnimationEvent
import com.android.szparag.todoist.events.ListScrollEvent
import com.android.szparag.todoist.models.entities.RenderDay
import io.reactivex.Observable

typealias UnixTimestamp = Long

interface FrontView : View {

  fun animateShowBackgroundImage(): Observable<AnimationEvent>
  fun animateShowQuote(): Observable<AnimationEvent>
  fun animatePeekCalendar(): Observable<AnimationEvent>

  fun subscribeDayListScrolls(): Observable<ListScrollEvent>
  fun subscribeBackgroundClicked(): Observable<Any>
  fun subscribeDayClicked(): Observable<UnixTimestamp>

  fun appendRenderDays(appendingDays: Collection<RenderDay>, fromIndex: Int, changedElementsCount: Int)

  fun randomizeContents()

}