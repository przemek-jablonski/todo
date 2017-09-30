package com.android.szparag.todoist.views.contracts

import com.android.szparag.todoist.AnimationEvent
import io.reactivex.Observable

interface FrontView : View{

  fun animateShowBackgroundImage(): Observable<AnimationEvent>
  fun animateShowQuote(): Observable<AnimationEvent>
  fun animatePeekCalendar(): Observable<AnimationEvent>

}